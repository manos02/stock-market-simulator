module messagequeue {
    exports nl.rug.aoop.messagequeue.message;
    exports nl.rug.aoop.messagequeue.queues;
    exports nl.rug.aoop.messagequeue.messageQueueCommandHandler;
    exports nl.rug.aoop.messagequeue.mqconsumer;
    requires org.slf4j;
    requires static lombok;
    requires com.google.gson;
    requires networking;
    requires command;
    requires org.mockito;
}