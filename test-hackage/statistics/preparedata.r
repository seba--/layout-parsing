raw0 <- read.csv("../evaluation-data/all1367191170785.csv", sep=";")
raw1 <- subset(raw0,select=c(path, run, time, byte.size,lines.of.code,all.success,reference.time))
raw2 <- subset(read.csv("../evaluation-data/all1367191170785.csv", sep=";"),select=c(time,all.success,reference.time))
raw3 <- subset(read.csv("../evaluation-data/all1367191170785.csv", sep=";"),select=c(time,all.success,reference.time))
# HACK: raw1,raw2,raw3 are the same

colnames(raw1)<-c("path","run","time.a","size","lines","all.success.a","reftime.a")
colnames(raw2)<-c("time.b","all.success.b","reftime.b")
colnames(raw3)<-c("time.c","all.success.c","reftime.c")

raw123<-cbind(raw1,raw2,raw3)

parseable<-subset(raw123,all.success.a=="true" & all.success.b=="true" & all.success.c=="true")


p1<-subset(parseable,run==1,select=c(path, time.a, time.b, time.c, size,lines,reftime.a,reftime.b,reftime.c))
p2<-subset(parseable,run==2,select=c( time.a, time.b, time.c, size,lines))
p3<-subset(parseable,run==3,select=c( time.a, time.b, time.c, size,lines))
colnames(p1)<-c("path","time1a","time1b","time1c","size1","lines1","reftime.a","reftime.b","reftime.c")
colnames(p2)<-c("time2a","time2b","time2c","size2","lines2")
colnames(p3)<-c("time3a","time3b","time3c","size3","lines3")
c<-cbind(p1,p2,p3)

errfun<-function(v) {
  return(qt(0.95,df=length(v)-1)*sd(v)/sqrt(length(v)))
}

t1<-c("time1a","time1b","time1c")
t2<-c("time2a","time2b","time2c")
t3<-c("time3a","time3b","time3c")
tr<-c("reftime.a","reftime.b","reftime.c")

x=apply(c[,t1],1,FUN=mean)
x<-cbind(x,apply(c[,t1],1,FUN=sd))
x<-cbind(x,apply(c[,t1],1,FUN=errfun))
x<-cbind(x,apply(c[,t2],1,FUN=mean))
x<-cbind(x,apply(c[,t2],1,FUN=sd))
x<-cbind(x,apply(c[,t2],1,FUN=errfun))
x<-cbind(x,apply(c[,t3],1,FUN=mean))
x<-cbind(x,apply(c[,t3],1,FUN=sd))
x<-cbind(x,apply(c[,t3],1,FUN=errfun))
x<-cbind(x,apply(c[,tr],1,FUN=mean))
x<-cbind(x,apply(c[,tr],1,FUN=sd))
x<-cbind(x,apply(c[,tr],1,FUN=errfun))
colnames(x)<-c("mean1","sd1","err1","mean2","sd2","err2","mean3","sd3","err3","mean.r","sd.r","err.r")
c<-cbind(c,x)

