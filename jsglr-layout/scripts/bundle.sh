#! /bin/bash -x

mkdir -p staging/
rsync -r ../src/ staging/
rsync -r ../war/WEB-INF/classes/ staging/
jar cvf jsglr-gwt-$(date +"%Y-%m-%d").jar -C staging/ .
  