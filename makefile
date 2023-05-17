# JAVA_VERSION = 20
JAVA_C = javac
JAVA = java

#JC_FLAGS = -g
#J_FLAGS = -ea

JAVA_MAIN_CLASS = FiniteAutomaton
JAVA_SOURCES = $(wildcard src/main/java/**/**/*.java)
JAVA_CLASSES = $(patsubst src/main/java/%.java, target/classes/%.class, $(JAVA_SOURCES))

# Compile the Java source files
compile: $(JAVA_CLASSES)
	$(info Java source files: $(JAVA_SOURCES))
	$(info Java class files: $(JAVA_CLASSES))

# Run the Java main class
run: compile
	$(JAVA) -cp target/classes $(JAVA_MAIN_CLASS)

# Clean the target directory
clean:
	rm -rf target

# Compile the Java source files
target/classes/%.class: src/main/java/%.java
	$(JAVA_C) -d target/classes $<

# Create the target directory
target/classes:
	mkdir -p target/classes

# Make the target directory a dependency of the Java class files
$(JAVA_CLASSES): target/classes
compile: target/classes
clean: target/classes
run: target/classes
default: target/classes

default: run
