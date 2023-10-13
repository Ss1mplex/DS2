JAVAC = javac
JAR_FILES = commons-beanutils-1.9.4.jar commons-collections-3.2.2.jar commons-lang-2.6.jar commons-logging-1.2.jar ezmorph-1.0.6.jar json-20230618.jar json-lib-2.4-jdk15.jar minimal-json-0.9.5.jar

SRC_DIR = src
BIN_DIR = bin

# List of Java source files
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Classpath for compilation
CLASSPATH = $(addprefix $(BIN_DIR)/,$(JAR_FILES))

# Targets for AggregationServer, ContentServer, LamportClock, and GETClient
AGGREGATION_SERVER = $(BIN_DIR)/AggregationServer.class
CONTENT_SERVER = $(BIN_DIR)/ContentServer.class
LAMPORT_CLOCK = $(BIN_DIR)/LamportClock.class
GET_CLIENT = $(BIN_DIR)/GETClient.class

all: $(AGGREGATION_SERVER) $(CONTENT_SERVER) $(LAMPORT_CLOCK) $(GET_CLIENT)

$(AGGREGATION_SERVER): $(SOURCES)
	$(JAVAC) -cp .:$(CLASSPATH) -d $(BIN_DIR) $^

$(CONTENT_SERVER): $(SOURCES)
	$(JAVAC) -cp .:$(CLASSPATH) -d $(BIN_DIR) $^

$(LAMPORT_CLOCK): $(SOURCES)
	$(JAVAC) -cp .:$(CLASSPATH) -d $(BIN_DIR) $^

$(GET_CLIENT): $(SOURCES)
	$(JAVAC) -cp .:$(CLASSPATH) -d $(BIN_DIR) $^

clean:
	rm -rf $(BIN_DIR)

.PHONY: all clean
