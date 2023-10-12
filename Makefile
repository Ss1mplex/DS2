# Define the Java compiler
JAVAC = javac

# Define the source directory
SRC_DIR = src

# Define the output directory
OUT_DIR = out

# Create the output directory
$(shell mkdir -p $(OUT_DIR))

# Find all .java files in the source directory
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Create the corresponding .class file paths
CLASSES = $(patsubst $(SRC_DIR)/%.java,$(OUT_DIR)/%.class,$(SOURCES))

# Default target: build all .class files
all: $(CLASSES)

# Compile .java files to .class files
$(OUT_DIR)/%.class: $(SRC_DIR)/%.java
	$(JAVAC) -d $(OUT_DIR) $<

# Clean the compiled .class files
clean:
	rm -rf $(OUT_DIR)

# Run the AggregationServer
run-aggregation-server:
	java -cp $(OUT_DIR) AggregationServer

# Run the ContentServer
run-content-server:
	java -cp $(OUT_DIR) ContentServer

# Run the GetClient
run-get-client:
	java -cp $(OUT_DIR) GetClient

.PHONY: all clean run-aggregation-server run-content-server run-get-client
