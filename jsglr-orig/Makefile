#DESTDIR = /
#PREFIX  = /
 
all:
	ant -f build.ant.xml jar

install: all
	ant -f build.ant.xml -Dprefix=$(DESTDIR)/$(PREFIX) install

dist:
	pwd
	ant -f build.ant.xml dist
	
check:
	echo "no check"
