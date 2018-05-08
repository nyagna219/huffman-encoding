JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Node.java \
	BinaryHeap.java \
	FourWayCache.java \
	Pairnode.java \
	    PairHeap.java \
	encoder.java \
	  Decnode.java \
	decoder.java

default: classes

classes: $(CLASSES:.java=.class)

clean: $(RM) *.class
