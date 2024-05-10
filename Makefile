JAVAC=/usr/bin/javac
.SUFFIXES: /java .class
SRCDIR=src/barScheduling
BINDIR=bin/


$(BINDIR)/%.class:$(SRCDIR)/*.java
		$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) -sourcepath $(SRCDIR) $^

CLASSES= Patron.class Barman.class DrinkOrder.class SchedulingSimulation.class

CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)
default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/SchedulingSimulation/*.class


run: $(CLASS_FILES)
	java -cp $(BINDIR) barScheduling.SchedulingSimulation
