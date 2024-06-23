<br />
<p align="center">
  <h1 align="center">Stock Market Simulation</h1>

  <p align="center">
    Slow motion wall street stock market
  </p>
</p>

## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
  * [Running](#running)
* [Modules](#modules)
* [Notes](#notes)
* [Evaluation](#evaluation)
* [Extras](#extras)

## About The Project

<!-- Add short description about the project here -->
This projects involves a stock market simulation betweeen traders and the stock market. Each trader can make an order(buy or sell)
and the stock market can handle these requests, updating the information. This is done via the client-server model using a network.
Also, this program runs on multiple threads resulting a better performance.


## Installation

To get a local copy up and running follow these simple steps.

### Prerequisites

* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher
* [Maven 3.6](https://maven.apache.org/download.cgi) or higher

## Steps

<pre>
git clone https://github.com/manos02/stock-market-simulator.git
cd battle-simulator
mvn clean install
</pre>

1. Navigate to the `stocks` directory
2. Clean and build the project using:
```sh
mvn install
```

## Running

<!--
Describe how to run your program here. These should be a few very simple steps.
-->

1) First start the main class of stock-exchange module
2) Then start the main class of trading module.

## Modules

<!--
Describe each module in the project, what their purpose is and how they are used in your program. Try to aim for at least 100 words per module.
-->
In our program we have 4 main modules: stock-exchange, trading, network, message-queue.

Stock-exchange: This module handles all the operations of the stock market and facilitates interactions between traders and the server. 
It is responsible for setting up the server and handling client connections through client-server communication. It is responsible for sending 
updates to the traders module continuously, through the StockApplication class. It also handles orders that are received
by the traders and ensures that orders are executed correctly based on the type of each order. Furthermore, it can resolve orders with each other
and update the relevant parties involved in each transaction. This is done by matching buy and sell orders to complete transactions. Endly it
maintains the transaction history for each trader and the order book.

Trading: This module handles all the operations of the traders and the orders. It is responsible for linking each trader with a client
and connect it to the stock-exchange module through the server. Each trader can handle updates from the stock market about themselves and the stocks. 
Also, each trader is continuously making an order with a random trading strategy, which is then sent to the stock market through the network.
When a trader's transaction is resolved, he immediately gets an update from the stock market and his assets are set appropriately.  

Network: This module handles all the operations of the client-server model. Firstly it sets up the server which has the ability
to handle multiple clients simultaneously. To achieve this, it spawns a new client handler for each incoming client connection.
This architecture ensures that the server can manage and serve a large number of clients concurrently. Also, the network module
facilitates communication between clients and the server. It can handle the exchange of data using message objects that are 
converted to json strings. For the stock market it is essential for transmitting orders from traders to the stock market, 
updating traders with market information, and managing various interactions within the system.

Message-queue: This module handles the message queues and their operations. It also facilitates the message class which is the 
outer layer of all the data sent over the network and encapsulates data and commands in the stock market simulation.
Messages can be easily converted to strings using Gson, making them suitable for transmission over the network
and back to message objects when they arrive to their destination.


## Design

<!--
List all the design patterns you used in your program. For every pattern, describe the following:
- Where it is used in your application.
- What benefit it provides in your application. Try to be specific here. For example, don't just mention a pattern improves maintainability, but explain in what way it does so.
-->
The most frequently used design pattern in our program is the behavioral command pattern, which plays a crucial role in both the stock and traders modules.
This pattern is used to encapsulate requests as objects, allowing the separation of the sender of a request from the receiver.
In the stock module, the command pattern is used to handle and execute orders submitted by traders.
Each order, such as a 'Buy' or 'Sell' order, is encapsulated as a command object. This enables the stock module to process these orders without needing to
know the specific details of each order. The benefit here is a high level of flexibility and extensibility.
This allows us to add new order types or modify existing ones without altering the core logic of the stock module. If we add more orders in the
future we will just need to add their functionality inside their respective classes and the stock-market will do the same tasks as now. Continuously
poll from the message queue and then match the command and execute it.
In the traders module, the command pattern is used for receiving messages from the stock market.
Traders can constantly receive updates about their portfolio, stock prices, and transaction history. Each command has its own class
and is responsible for processing the information received from the stock market. By doing so, the traders module can process
these updates without concrete knowledge of their specific content. If new types of updates are introduced in the future,
the traders module can easily integrate them without needing to change its core functionality. This supports long-term sustainability and 
single responsibility principle as each class is responsible for handling only one type of message.

Another design patter used in our program is the adapter pattern. It is used for two key purposes:
converting custom objects (Message and Order) to JSON format and vice versa. By creating these adapter classes we allow
Gson to interact with our custom objects that initially didn't support. It maintains the separation of concerns between the Message/Order class
and the serialization process. Gson is unaware of the internal structure of the Message class, so the adapter bridges this gap. 
This pattern provides flexibility in our program. This happens because if we decide to change the implementation of Message or Order
we only need to change their adepter classes. Furthermore, we can work with external libraries using their functionality without
worrying about it in the implementation of the actual object. Endly it establishes single responsibility principle as we are able 
to separate the data conversion from the actual logic of the particular class.  As a result, the program's components are cleaner,
more maintainable, and have distinct responsibilities.

Finally, we use the factory creational pattern for adding command handlers with different purposes(Message queue command-handler, 
stock market command handler). The AbstractCommandHandler interface has a factory method createCommandHandler, which returns a
specific instance of a CommandHandler. The purpose of this pattern is to encapsulate the creation of these command handlers
and allow for flexibility and customization in the creation process. The advantages are that we can introduce new commandhandlers
into the program without breaking existing code. This flexibility in extending functionality makes our program more adaptable 
and maintainable.
The Factory Pattern reduces the coupling between the client code and the specific command handler implementations.
Clients(in this case message queue and stock market) request command handlers through the factory method without needing
to know the intricate details of their creation. This loose coupling minimizes dependencies and makes less prone to
from changes and more clean and readable. Once again we develop the singe responsibility principle through
separation of concerns. The Factory Pattern aligns with the principle of separating concerns within the program.
It separates the responsibilities of creating command handlers from their actual functionality.
This separation improves code modularity, making it easier to comprehend and maintain.


## Evaluation

<!--
Discuss the stability of your implementation. What works well? Are there any bugs? Is everything tested properly? Are there still features that have not been implemented? Also, if you had the time, what improvements would you make to your implementation? Are there things which you would have done completely differently? Try to aim for at least 250 words.
-->

Our program is pretty stable and works as expected. There are multiple modules each responsible for a particular task and their
interactions create the stock market simulation. We almost have everywhere exception throwing and error handling to ensure that our program
works correctly and to easily detect the source of any errors. The core functionality of our program(the stock exchange, traders and 
queues) work well handling edge cases. Each trader has their transaction history and the stock market is able to maintain each of the orders
placed. Our program doesn't seem to crash after running the simulation for a long amount of time which suggests that it is stable. 
We implemented 3 types of testing.
Unit testing: We implemented unit testing using junit framework that tests each method separately. This ensures that our methods
work as intended and can handle edge cases correctly.
Integration testing: We also implemented integration testing to ensure that multiple components can work together and produce the 
desired output. This method of testing is very important to validate that the different software components work together as a system
to achieve desired functionality. We created multiple tests for buy orders and sell orders in the stock-exchange module and checked that orders 
are updated as expected.
Endurance Testing: By running our program for a significant amount of time, multiple times we are able to test its stability by ensuring that it does
not crash or produce unexpected errors.
If we had more time we would probably improve the design of our program. Also in some cases our program may be inefficient because
there are some functions which implement searching in a list. A potential solution would be to make some lists into hashmaps but this
may have result other implications. We implemented this for the orders and improved the efficiency significantly.
With the current amount of traders and stocks this is not a major issue but if we add more clients this 
could lead to some problems. Also, we could change how the transactions are handled in the trader module. Currently, each trader
has to check the last transaction received and then if it is resolved. Next he has to loop through all his transactions and update
the correct one using the uniqueId. This technique works but using some other type of data structure this procedure could be much more efficient.


## Extras

<!--
If you implemented any extras, you can list/mention them here.
-->

___


<!-- Below you can find some sections that you would normally put in a README, but we decided to leave out (either because it is not very relevant, or because it is covered by one of the added sections) -->

<!-- ## Usage -->
<!-- Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources. -->

<!-- ## Roadmap -->
<!-- Use this space to show your plans for future additions -->

<!-- ## Contributing -->
<!-- You can use this section to indicate how people can contribute to the project -->

<!-- ## License -->
<!-- You can add here whether the project is distributed under any license -->


<!-- ## Contact -->
<!-- If you want to provide some contact details, this is the place to do it -->

<!-- ## Acknowledgements  -->
