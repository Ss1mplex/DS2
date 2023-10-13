# Makefile for your Java project

# Define the source directory
SRC_DIR = src

# Define the Java compiler and flags
JAVAC = javac
JAVAC_FLAGS = -cp .:$(wildcard *.jar) -d bin

# Define the main classes for your applications
AGGREGATION_SERVER = AggregationServer
CONTENT_SERVER = ContentServer
LAMPORT_CLOCK = LamportClock
GET_CLIENT = GETClient

# Define the output directory
BIN_DIR = bin

# List of all Java source files
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# List of all compiled class files
CLASSES = $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Main target
all: $(CLASSES)

# Compile Java source files
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	$(JAVAC) $(JAVAC_FLAGS) $<

# Target to run the Aggregation Server
run-aggregation-server:
	java -cp .:$(BIN_DIR):$(wildcard *.jar) $(AGGREGATION_SERVER)

# Target to run the Content Server
run-content-server:
	java -cp .:$(BIN_DIR):$(wildcard *.jar) $(CONTENT_SERVER)

# Target to run the Lamport Clock
run-lamport-clock:
	java -cp .:$(BIN_DIR):$(wildcard *.jar) $(LAMPORT_CLOCK)

# Target to run the GET Client
run-get-client:
	java -cp .:$(BIN_DIR):$(wildcard *.jar) $(GET_CLIENT)

# Clean compiled class files
clean:
	rm -rf $(BIN_DIR)/*.class
