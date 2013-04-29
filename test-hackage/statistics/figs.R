source("preparedata.r")

reftimefix<-0

#comparison of times, without outliers
pdf("../sle12/compare.pdf")
boxplot((c$mean.r-reftimefix)/1000000,c$mean1/1000000,c$mean2/1000000,c$mean3/1000000,outline=FALSE,names=c("Reference parser","Parser 1","Parser 2","Parser 3"),ylab="time (in ms)")
dev.off()
embedFonts("../sle12/compare.pdf",options="-dSubsetFonts=true -dEmbedAllFonts=true -dSAFER -dNOPLATFONTS -dPDFSETTINGS=/printer -dMaxSubsetPct=100")

#slowdown
pdf("../sle12/slowdownIE.pdf",width=3.5,height=1.2)
par(mar=c(2.5,1.5,.1,1.8),ps=18)
boxplot(c$mean3/c$mean1,outline=FALSE,horizontal=TRUE,type="o",frame.plot=FALSE)
#,xlab="Slowdown with implicit layout parser"
dev.off()
embedFonts("../sle12/slowdownIE.pdf",options="-dSubsetFonts=true -dEmbedAllFonts=true -dSAFER -dNOPLATFONTS -dPDFSETTINGS=/printer -dMaxSubsetPct=100")

pdf("../sle12/slowdownOE.pdf",width=3.5,height=1.2)
par(mar=c(2.5,1.5,.1,1.8),ps=18)
boxplot(c$mean2/c$mean1,outline=FALSE,horizontal=TRUE,type="o",frame.plot=FALSE)
#,xlab="Slowdown with implicit layout parser"
dev.off()
embedFonts("../sle12/slowdownOE.pdf",options="-dSubsetFonts=true -dEmbedAllFonts=true -dSAFER -dNOPLATFONTS -dPDFSETTINGS=/printer -dMaxSubsetPct=100")

pdf("../sle12/slowdownIO.pdf",width=3.5,height=1.2)
par(mar=c(2.5,1.5,.1,1.8),ps=18)
boxplot(c$mean3/c$mean2,outline=FALSE,horizontal=TRUE,type="o",frame.plot=FALSE)
#,xlab="Slowdown with implicit layout parser"
dev.off()
embedFonts("../sle12/slowdownIo.pdf",options="-dSubsetFonts=true -dEmbedAllFonts=true -dSAFER -dNOPLATFONTS -dPDFSETTINGS=/printer -dMaxSubsetPct=100")



#time/size clouds 
pdf("../sle12/cloud.pdf")
plot(c$size1/1000,c$mean1/1000000,pch=".",log="xy",col="blue",xlab="Size (in kb)",ylab="Time (in ms)")
points(c$size3/1000,c$mean3/1000000,pch=".",col="red")
points(c$size3/1000,(c$mean.r-reftimefix)/1000000,pch=".",col="gray")
legend("bottomright",c("parser 1","parser 3","reference parser"),col=c("blue","red","gray"),pch=16)
dev.off()
embedFonts("../sle12/cloud.pdf",options="-dSubsetFonts=true -dEmbedAllFonts=true -dSAFER -dNOPLATFONTS -dPDFSETTINGS=/printer -dMaxSubsetPct=100")

#verteilung
v1<-c()
for (i in 0:500) v1<-c(v1,length(subset(c,mean1<i*1000000)$path)/length(c$path))
v2<-c()
for (i in 0:500) v2<-c(v2,length(subset(c,mean2<i*1000000)$path)/length(c$path))
v3<-c()
for (i in 0:500) v3<-c(v3,length(subset(c,mean3<i*1000000)$path)/length(c$path))
vr<-c()
for (i in 0:500) vr<-c(vr,length(subset(c,(mean.r-reftimefix)<i*1000000)$path)/length(c$path))
pdf("../sle12/distrib.pdf",width=7,height=6)
par(ps=18,lwd=2)
plot(vr*100,type="l",col="black",lty=3, ylab="Files parsed in given time (in percent)",xlab="Time (in ms)",bty="n")
points(v1*100,type="l",col="black",lty=2)
points(v3*100,type="l",col="black")
points(v2*100,type="l",col="darkgray",lty=4)
legend("bottomright",c("GHC","SDF-Expl","SDF-Orig","SDF-Impl"),
       col=c("black","black","darkgray","black"),lty=c(3,2,4,1))
dev.off()
embedFonts("../sle12/distrib.pdf",options="-dSubsetFonts=true -dEmbedAllFonts=true -dSAFER -dNOPLATFONTS -dPDFSETTINGS=/printer -dMaxSubsetPct=100")