#!/bin/bash


#default values, overridden by command line
writableBuildRoot=/home/buildHomes/drip
supportDir=$writableBuildRoot/support
builderDir=$supportDir/com.zizibujuan.drip.server.releng
basebuilderBranch=R3_7
publish=""
user=aniefer
resultsEmail=orion-releng@eclipse.org

buildType=I
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


updateRelengProject(){
	pushd $supportDir
	
	if [[ -d com.zizibujuan.drip.server.releng ]]; then
		rm -rf com.zizibujuan.drip.server.releng
	fi
	
	if [ "$buildType" == "N" -o -n "$noTag" ]; then 
		pushd /home/git/baosuzhai
		git pull
		popd
	fi
	
	echo "[`date +%H\:%M\:%S`] Get com.zizibujuan.drip.server.releng"
	cp -r /home/git/baosuzhai/server/releng/com.zizibujuan.drip.server.releng .
	
	echo "[`date +%H\:%M\:%S`] Done getting com.zizibujuan.drip.server.releng"
	popd
}

setProperties(){
	echo 'setProperties'
}

runBuild(){
	cmd="java -version"
	echo "[`date +%H\:%M\:%S`] Launching Build"
	$cmd
	echo "[`date +%H\:%M\:%S`] Build Complete"
}

updateRelengProject
setProperties
runBuild
