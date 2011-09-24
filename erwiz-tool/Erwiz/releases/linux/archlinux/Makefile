release:	clean copy
	makepkg --source

echo:
	echo "test"

release-from-ant:
	pwd

install:	copy
	makepkg -i

copy:
	#cp ../erwiz-tool/erwiz-0.9.0-src.tar.gz .
	#@echo "src: "
	#@md5sum erwiz-0.9.0-src.tar.gz
	cp ../../single/erwiz-*.*.*-bin.tar.gz .
	#@echo "bin: "
	#@md5sum erwiz-0.9.0-bin.tar.gz

clean:
	rm -rf erwiz* pkg src
