#!/bin/bash


#default values, overridden by command line
writableBuildRoot=/home/buildHomes/drip
supportDir=$writableBuildRoot/support
builderDir=$supportDir/com.zizibujuan.drip.server.releng
basebuilderBranch=R3_7
publish=""
user=aniefer
resultsEmail=orion-releng@eclipse.org

buildType=N
date=$(date +%Y%m%d)
time=$(date +%H%M)
timestamp=$date$time

javaHome=""
launcherJar=""

buildLabel=""

tagMaps=""
compareMaps=""
fetchTag=""
publish=""
javase170=""


tagMaps=""
compareMaps=""
fetchTag="-DfetchTag=CVS=HEAD,GIT=master"
publish="-DpublishToEclipse=false"




updateBaseBuilder () {
	echo "**********************update base builder begin**********************"
    pushd $supportDir
    if [[ ! -d org.eclipse.releng.basebuilder_${basebuilderBranch} ]]; then
        echo "[start - `date +%H\:%M\:%S`] Get org.eclipse.releng.basebuilder_${basebuilderBranch}"
        cmd="cvs -d :pserver:anonymous@dev.eclipse.org:/cvsroot/eclipse $quietCVS ex -r $basebuilderBranch -d org.eclipse.releng.basebuilder_${basebuilderBranch} org.eclipse.releng.basebuilder"
        echo $cmd
        $cmd
        echo "[finish - `date +%H\:%M\:%S`] Done getting org.eclipse.releng.basebuilder_${basebuilderBranch}"
    fi

    echo "[`date +%H\:%M\:%S`] Getting org.eclipse.releng.basebuilder_${basebuilderBranch}"
    rm org.eclipse.releng.basebuilder
    ln -s ${supportDir}/org.eclipse.releng.basebuilder_${basebuilderBranch} org.eclipse.releng.basebuilder
    echo "[`date +%H\:%M\:%S`] Done setting org.eclipse.releng.basebuilder"
	popd
	echo "**********************update base builder end**********************"
}


updateRelengProject(){
	echo "**********************update releng project begin**********************"
	
	echo "cd $supportDir"
	pushd $supportDir
	ls
	
	
	
	if [[ -d com.zizibujuan.drip.server.releng ]]; then
		echo "check if exist com.zizibujuan.drip.server.releng in current directory"
		echo "exist com.zizibujuan.drip.server.releng, then delete"
		rm -rf com.zizibujuan.drip.server.releng
	fi
	
	
	if [ "$buildType" == "N" -o -n "$noTag" ]; then 
		echo "check if buildType == N and noTag length == 0"
		echo "git pull latest code"
		pushd /home/git/baosuzhai
		git pull
		popd
	fi
	
	echo "[`date +%H\:%M\:%S`] Get com.zizibujuan.drip.server.releng"
	cp -r /home/git/baosuzhai/server/releng/com.zizibujuan.drip.server.releng .
	
	echo "[`date +%H\:%M\:%S`] Done getting com.zizibujuan.drip.server.releng"
	popd
	echo "**********************update releng project end**********************"
}

setProperties(){
	echo "**********************set properties begin**********************"
	buildDirectory=$writableBuldRoot/$buildType$timestamp
	buildLabel=$buildType$date-$time
	javaHome=/usr/lib64/jvm/java-7-oracle
	
	pushd $supportDir
	ls
	launcherJar=$supportDir/$( find org.eclipse.releng.basebuilder/ -name "org.eclipse.equinox.launcher_*.jar" | sort | head -1 )
	echo "$launcherJar"
	popd
		
	#Properties for compilation boot classpaths
	JAVA70_HOME=/usr/lib64/jvm/java-7-oracle

	javase170="$JAVA70_HOME/jre/lib/resources.jar:$JAVA70_HOME/jre/lib/rt.jar:$JAVA70_HOME/jre/lib/jsse.jar:$JAVA70_HOME/jre/lib/jce.jar:$JAVA70_HOME/jre/lib/charsets.jar"
	echo "**********************set properties end**********************"
}

runBuild(){
	echo "**********************run build begin**********************"
	cmd="$javaHome/bin/java -enableassertions -jar $launcherJar \
			-application org.eclipse.ant.core.antRunner \
			-buildfile $builderDir/buildWebIDE.xml \
			-Dbuilder=$builderDir/builder \
			-Dbase=$writableBuildRoot \
			-DbuildType=$buildType -Dtimestamp=$timestamp -DbuildLabel=$buildLabel \
			-DgitUser=$user \
			$tagMaps $compareMaps $fetchTag $publish \
			-DJavaSE-1.7=$javase170"
			
	echo "[`date +%H\:%M\:%S`] Launching Build"
	$cmd
	echo "[`date +%H\:%M\:%S`] Build Complete"
	
	#stop now if the build failed
	failure=$(sed -n '/BUILD FAILED/,/Total time/p' $writableBuildRoot/logs/current.log)
	if [[ ! -z $failure ]]; then
		compileMsg=""
		prereqMsg=""
		pushd $buildDirectory/plugins
		compileProblems=$( find . -name compilation.problem | cut -d/ -f2 )
		popd
		
		if [[ ! -z $compileProblems ]]; then
			compileMsg="Compile errors occurred in the following bundles:"
		fi
		if [[ -e $buildDirectory/prereqErrors.log ]]; then
			prereqMsg=`cat $buildDirectory/prereqErrors.log` 
		fi
		
		mailx -s "Drip Build : $buildLabel failed" $resultsEmail <<EOF
$compileMsg
$compileProblems

$prereqMsg

$failure
EOF
		exit
	fi
	
	echo "**********************run build end**********************"
}

updateRelengProject
updateBaseBuilder
setProperties
runBuild
