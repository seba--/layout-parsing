outfile<- "../evaluation-data/stats.tex"

source("preparedata.r")

parseableghc<-subset(raw0,explicit.layout.OK=="true" & ambInfix.error=="false")
allfiles<-length(raw0$path)/3
parseghc<-length(parseableghc$path)/3

parse1<-subset(parseableghc,run==1,select=c(path, parse.ok,stack.overflow , timeout, diffs.to.1, all.success))
parse2<-subset(parseableghc,run==2,select=c(path, parse.ok,stack.overflow , timeout, diffs.to.1))
parse3<-subset(parseableghc,run==3,select=c(path, parse.ok,stack.overflow , timeout, diffs.to.1))
colnames(parse1)<-c("path","ok1","stack1","timeout1", "diffs1","all.success")
colnames(parse2)<-c("path","ok2","stack2","timeout2", "diffs2")
colnames(parse3)<-c("path","ok3","stack3","timeout3", "diffs3")
merged<-merge(merge(parse1,parse2),parse3)
parseable<-subset(merged,ok1=="true" & ok2=="true" & ok3=="true" & diffs2==0 & diffs3==0)
parseall<-length(parseable$path)


write("%values exported from R. do not modify manually",outfile)

printlabel<-function(label, value) 
{
  write(paste("\\newcommand\\",label,"{",value,"}",sep=""),file=outfile,append=TRUE)
}
formatd <- function(v) {
  return(formatC(v,big.mark = "\\\\,", decimal.mark = ".",format="d"))
}

# all files, including those not parseable by ghc
printlabel("numHaskellFiles",formatC(allfiles,big.mark = "\\\\,", decimal.mark = ".",format="d"))
printlabel("numPackages",length(unique(raw0$package)))
printlabel("sizeHaskellLines",formatC(sum(subset(raw0,run==2)$lines.of.code),big.mark = "\\\\,", decimal.mark = ".",format="d"))
printlabel("sizeHaskellMB",round(sum(subset(raw0,run==2)$byte.size/1000000)))

printlabel("numNotGHCFiles",allfiles-parseghc)
printlabel("numNotGHCFilesPercent",round((allfiles-parseghc)/allfiles*100))
printlabel("numGHCFiles",formatC(parseghc,big.mark = "\\\\,", decimal.mark = ".",format="d"))
printlabel("numAllOKFiles",formatC(parseall,big.mark = "\\\\,", decimal.mark = ".",format="d"))
printlabel("numAllOKFilesPercent",round(parseall/parseghc*100))


printlabel("geoMeanSlowdownIE",round(exp(mean(log(c$mean3/c$mean1))),digits=1))
printlabel("worstSlowdownIE",round(max(c$mean3/c$mean1)))

printlabel("geoMeanSlowdownOE",round(exp(mean(log(c$mean2/c$mean1))),digits=1))
printlabel("worstSlowdownOE",round(max(c$mean2/c$mean1)))

printlabel("geoMeanSlowdownIO",round(exp(mean(log(c$mean3/c$mean2))),digits=1))
printlabel("worstSlowdownIO",round(max(c$mean3/c$mean2)))


printlabel("numBytePa",formatd(round(sum(c$size1)/1000)))#in kb
printlabel("numBytePb",formatd(round(sum(c$size2)/1000)))
printlabel("numBytePc",formatd(round(sum(c$size3)/1000)))
printlabel("numLinesPa",formatd(round(sum(c$lines1))))
printlabel("numLinesPb",formatd(round(sum(c$lines2))))
printlabel("numLinesPc",formatd(round(sum(c$lines3))))
printlabel("totalTimePa",formatd(round(sum(c$mean1)/1000000000)))#in sec
printlabel("totalTimePb",formatd(round(sum(c$mean2)/1000000000)))
printlabel("totalTimePc",formatd(round(sum(c$mean3)/1000000000)))
printlabel("totalTimePr",formatd(round(sum(c$mean.r)/1000000000)))
printlabel("medianTimePa",formatd(round(median(c$mean1)/1000000)))#in msec
printlabel("medianTimePb",formatd(round(median(c$mean2)/1000000)))
printlabel("medianTimePc",formatd(round(median(c$mean3)/1000000)))
printlabel("medianTimePr",formatd(round(median(c$mean.r)/1000000)))
printlabel("performPa",round(sum(c$size1)/1000/(sum(c$mean1)/1000000000)))#in kb/sec
printlabel("performPb",round(sum(c$size2)/1000/(sum(c$mean2)/1000000000)))
printlabel("performPc",round(sum(c$size3)/1000/(sum(c$mean3)/1000000000)))
printlabel("performPr",round(sum(c$size3)/1000/(sum(c$mean.r)/1000000000)))

printlabel("byteIncreaseEO",formatd(round(sum(c$size1)/sum(c$size2)*100-100)))#in kb
printlabel("byteDecreaseOI",formatd(round(100-sum(c$size3)/sum(c$size2)*100)))
