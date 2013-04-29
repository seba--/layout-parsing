source("http://bioinfo-mite.crb.wsu.edu/Rcode/Venn.R")

raw <- read.csv("all1337760874935.csv", sep=";")
parseableghc<-raw
#subset(raw,explicit.layout.OK=="true" & ambInfix.error=="false")

p1<-subset(parseableghc,run==1,select=c(path, parse.ok,stack.overflow , timeout,explicit.layout.OK,ambInfix.error))
p2<-subset(parseableghc,run==2,select=c(path, parse.ok,stack.overflow , timeout))
p3<-subset(parseableghc,run==3,select=c(path, parse.ok,stack.overflow , timeout))

colnames(p1)<-c("path","ok1","stack1","timeout1","elo","aie")
colnames(p2)<-c("path","ok2","stack2","timeout2")
colnames(p3)<-c("path","ok3","stack3","timeout3")

c<-merge(merge(p1,p2),p3)

# okp1p2p3<-subset(c,(ok1=="true") & (ok2=="true") & (ok3=="true"))
# okp1p2<-subset(c,(ok1=="true") & (ok2=="true") & (ok3=="false"))
# okp2p3<-subset(c,(ok1=="false") & (ok2=="true") & (ok3=="true"))
# okp1p3<-subset(c,(ok1=="true") & (ok2=="false") & (ok3=="true"))
# okp1<-subset(c,(ok1=="true") & (ok2=="false") & (ok3=="false"))
# okp2<-subset(c,(ok1=="false") & (ok2=="true") & (ok3=="false"))
# okp3<-subset(c,(ok1=="false") & (ok2=="false") & (ok3=="true"))
# oknone<-subset(c,(ok1=="false") & (ok2=="false") & (ok3=="false"))

isGHC<-(c$elo=="true" & c$aie=="false")
isOk1<-(c$ok1=="true")
isOk2<-(c$ok2=="true")
isOk3<-(c$ok3=="true")
isOkAll<-cbind(isGHC,isOk1,isOk2,isOk3)
a<-vennCounts(isOkAll)
vennDiagram(a,names=c("GHC","Parser 1","Parser 2","Parser 3"),cex=1)


