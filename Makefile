# Define the Java compiler
JAVAC = javac

# Define the options for the Java compiler
JAVACFLAGS = -cp .:commons-beanutils-1.9.4.jar:commons-lang-2.6.jar:commons-logging-1.2.jar:ezmorph-1.0.6.jar:json-20230618.jar:json-lib-2.4-jdk15.jar:minimal-json-0.9.5.jar

# Define the source directory
SRC_DIR = src

# Define the build directory
BIN_DIR = bin

# Define the Java source files
JAVA_FILES = $(wildcard $(SRC_DIR)/*.java)

# Define the target classes
CLASSES = $(JAVA_FILES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# The default target to build all classes
all: $(CLASSES)

# Build the classes from source
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	$(JAVAC) $(JAVACFLAGS) -d $(BIN_DIR) $<

# Clean the compiled classes
clean:
	rm -rf $(BIN_DIR)/*.class
