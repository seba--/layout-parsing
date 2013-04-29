source("http://bioinfo-mite.crb.wsu.edu/Rcode/Venn.R")

raw <- read.csv("all1339404021207.csv", sep=";")
parseableghc<-subset(raw,explicit.layout.OK=="true" & ambInfix.error=="false")

p1<-subset(parseableghc,run==1,select=c(path, parse.ok,stack.overflow , timeout, diffs.to.1, all.success))
p2<-subset(parseableghc,run==2,select=c(path, parse.ok,stack.overflow , timeout, diffs.to.1))
p3<-subset(parseableghc,run==3,select=c(path, parse.ok,stack.overflow , timeout, diffs.to.1))

colnames(p1)<-c("path","ok1","stack1","timeout1", "diffs1","all.success")
colnames(p2)<-c("path","ok2","stack2","timeout2", "diffs2")
colnames(p3)<-c("path","ok3","stack3","timeout3", "diffs3")

c<-merge(merge(p1,p2),p3)

# okp1p2p3<-subset(c,(ok1=="true") & (ok2=="true") & (ok3=="true"))
# okp1p2<-subset(c,(ok1=="true") & (ok2=="true") & (ok3=="false"))
# okp2p3<-subset(c,(ok1=="false") & (ok2=="true") & (ok3=="true"))
# okp1p3<-subset(c,(ok1=="true") & (ok2=="false") & (ok3=="true"))
# okp1<-subset(c,(ok1=="true") & (ok2=="false") & (ok3=="false"))
# okp2<-subset(c,(ok1=="false") & (ok2=="true") & (ok3=="false"))
# okp3<-subset(c,(ok1=="false") & (ok2=="false") & (ok3=="true"))
# oknone<-subset(c,(ok1=="false") & (ok2=="false") & (ok3=="false"))

isOk1<-(c$ok1=="true" & c$diffs1==0)
isOk2<-(c$ok2=="true" & (c$ok1=="false" | c$diffs2==0))
isOk3<-(c$ok3=="true" & (c$ok1=="false" | c$diffs3==0))
isOkAll<-cbind(isOk1,isOk2,isOk3)
a<-vennCounts(isOkAll)

pdf("../sle12/venn.pdf",width=7,height=3.5)
par(ps=12,lwd=1,mar=c(0,0,0,0))
vennDiagram(a,names=c("SGLR-Expl","SGLR-Orig","SGLR-Impl"),cex=1)
dev.off()
embedFonts("../sle12/venn.pdf",options="-dSubsetFonts=true -dEmbedAllFonts=true -dSAFER -dNOPLATFONTS -dPDFSETTINGS=/printer -dMaxSubsetPct=100")


isTimeout1<-(c$timeout1=="true")
isTimeout2<-(c$timeout2=="true")
isTimeout3<-(c$timeout3=="true")
isTimeoutAll<-cbind(isTimeout1,isTimeout2,isTimeout3)
a2<-vennCounts(isTimeoutAll)

pdf("../sle12/timeouts.pdf",width=7,height=3.5)
par(ps=12,lwd=1,mar=c(0,0,0,0))
vennDiagram(a2,names=c("SGLR-Expl","SGLR-Orig","SGLR-Impl"),cex=1)
dev.off()
embedFonts("../sle12/timeouts.pdf",options="-dSubsetFonts=true -dEmbedAllFonts=true -dSAFER -dNOPLATFONTS -dPDFSETTINGS=/printer -dMaxSubsetPct=100")


cNoTimeout<-subset(c, timeout1=="false" & timeout2=="false" & timeout3=="false")
isOkNoTimeout1<-(cNoTimeout$ok1=="true" & cNoTimeout$diffs1==0)
isOkNoTimeout2<-(cNoTimeout$ok2=="true" & (cNoTimeout$ok1=="false" | cNoTimeout$diffs2==0))
isOkNoTimeout3<-(cNoTimeout$ok3=="true" & (cNoTimeout$ok1=="false" | cNoTimeout$diffs3==0))
isOkAllNoTimeout<-cbind(isOkNoTimeout1,isOkNoTimeout2,isOkNoTimeout3)
aNoTimeout<-vennCounts(isOkAllNoTimeout)

pdf("../sle12/vennNoTimeout.pdf",width=7,height=3.5)
par(ps=12,lwd=1,mar=c(0,0,0,0))
vennDiagram(aNoTimeout,names=c("SGLR-Expl","SGLR-Orig","SGLR-Impl"),cex=1)
dev.off()
embedFonts("../sle12/vennNoTimeout.pdf",options="-dSubsetFonts=true -dEmbedAllFonts=true -dSAFER -dNOPLATFONTS -dPDFSETTINGS=/printer -dMaxSubsetPct=100")
