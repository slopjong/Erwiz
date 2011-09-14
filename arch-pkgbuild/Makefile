all:	clean copy
	makepkg
	makepkg -i

copy:
	cp ../erwiz-tool/erwiz-0.9.0-src.tar.gz .
	md5sum erwiz-0.9.0-src.tar.gz
clean:
	rm -rf erwiz* pkg src