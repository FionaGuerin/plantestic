# Plantestic
![plantestic](https://img.shields.io/badge/🌱-plantestic-green.svg)
[![Build Status](https://travis-ci.com/FionaGuerin/plantestic.svg?token=qCz9ynu1x7xYBT4zA1MS&branch=master)](https://travis-ci.com/FionaGuerin/plantestic.svg?token=qCz9ynu1x7xYBT4zA1MS&branch=master)

## Description
The test case generator Plantestic produces test cases from a sequence diagram. 
A sequence diagram models a sequence of interactions between objects. 
A test case then checks for such an interaction whether it is implemented as the sequence diagram defines it. 
In an example sequence diagram called `Hello`, let there be two actors Alice and Bob. 
Alice sends Bob the request `GET /hello ` and Bob answers with `Hello World`. 
The corresponding test case now sends an HTTP request `GET /hello` to the backend. 
The test case then expects a response with status `200 OK` and date `Hello World`.

![./core/src/test/resources/minimal_hello.png](./core/src/test/resources/minimal_hello.png)

```
public void test() throws Exception {
		try {
			Response roundtrip1 = RestAssured.given()
					.auth().basic(substitutor.replace("${B.username}"), substitutor.replace("${B.password}"))
				.when()
					.get(substitutor.replace("${B.path}") + substitutor.replace("/hello"))
				.then()
					.assertThat()
					    .statusCode(IsIn.isIn(Arrays.asList(200)));
		} catch (Exception exception) {
			System.out.println("An error occured during evaluating the communication with testReceiver: ");
			exception.printStackTrace();
			throw exception;
		}
	}
```

## Motivation
The implementation of user requirements often deviates from the specification of the same user requirements. 
Individual work, teamwork, and collaboration between teams can produce such a divergence. 
For example, requirements may be misinterpreted or overlooked. 
Teamwork, especially with multiple teams, causes interface errors. 
For example, subsystems of the same product may use conflicting technologies or conflicting data formats.

Our test case generator detects deviations at an early stage: 
The test case generator derives test cases directly from the specification. 
If the implementation fulfills these test cases, then the implementation fulfills the specification. 
If the implementation does not fulfill these test cases, the implementation deviates from the specification. 
With our test case generator, developers can quickly uncover inconsistencies, fix them, and save costs.## Demo

## Features
Plantestic is universal in that it can run in any IDE. 
For this, Plantestic uses Gradle.

Plantestic is user-friendly: 
You set it up by installing Java and downloading Plantestic.
You generate a test case by filing a sequence diagram and entering one instruction. 

Plantestic has a powerful condition evaluation: 
A sequence diagram can contain alternative or optional interactions that it invokes under a certain condition. 
Plantestic will evaluate any condition that conforms to JavaScript. 
For this, it uses a JavaScript engine.  

You can pass parameters to your sequence diagram if you wish to customize its flows.
For example, you no longer need to reveal security-critical information such as passwords in your sequence diagram. 
Plantestic evaluates the parameters using templating.

## Technologies
### Eclipse Modelling Framework (EMF)
The Eclipse Modeling Framework serves modeling and code generation. 
From a model specification in XMI, it produces an equivalent set of classes in Java.  
Website: [https://www.eclipse.org/modeling/emf/](https://www.eclipse.org/modeling/emf/)

### Gradle
The build management system Gradle splits a project into tasks and executes these tasks in a build. 
An example of a task is code compilation. 
Gradle downloads and configures dependencies and libraries automatically.
Website: [https://gradle.org](https://gradle.org)

### XML Metadata Interchange (XMI)
XML Metadata Interchange is an exchange format among software development tools. 
For example, XML Metadata Interchange represents UML diagrams textually within the Eclipse Modeling Framework.  
Website: [https://www.omg.org/spec/XMI/About-XMI/](https://www.omg.org/spec/XMI/About-XMI/)

### PlantUML
The open source tool PlantUML produces a UML diagram from simple text language. 
Such a PlantUML diagram can be a sequence diagram, use case diagram, class diagram, activity diagram, component diagram, 
state diagram, object diagram, deployment diagram, or timing diagram.  
Website: [http://plantuml.com](http://plantuml.com)

### REST Assured
The Rest-assured library simplifies the verification and validation of REST APIs. 
As such, the test techniques of Rest-assured follow the test techniques of dynamic languages such as Ruby and Groovy. 
The library offers all standardized HTTP operations.  
Website: [http://rest-assured.io](http://rest-assured.io)

### Xtext
The Eclipse framework Xtext produces a domain-specific language from a grammar. 
On the one hand, Xtext generates a class diagram for the abstract syntax of the domain-specific language. 
On the other hand, Xtext provides a parser, a linker, a compiler, and a typechecker.  
Website: [https://www.eclipse.org/Xtext/](https://www.eclipse.org/Xtext/) 

### Query View Transformation (QVT)
The programming language set Query View Transformation describes model-to-model transformations. 
Query View Transformations implement the MOF (Meta Object Facility) query-view-transformation standard of the Object Management Group. 
Query View Transformation contains the languages Query View Transformation Operational, Query View Transformation Relations, and Query View Transformation Core. 
The imperative language Query-View-Transformation-Operational serves unidirectional model transformations.  
Website: [https://wiki.eclipse.org/QVTo](https://wiki.eclipse.org/QVTo)

### Acceleo
The model-to-text generator Acceleo produces source code from an EMF model. 
Acceleo is easy to use and it can be customized by adding text to the generated code. 
Acceleo implements the MOF (Meta Object Facility) model-to-text standard of the Object Management Group. 
Website: [https://www.eclipse.org/acceleo/](https://www.eclipse.org/acceleo/)

### WireMock
The WireMock tool simulates an HTTP API.
WireMock mocks an HTTP server whose services are used by a client. 
The HTTP server may therefore be missing or incomplete.
Website: [http://wiremock.org](http://wiremock.org)

## Abstract syntaxes
### PlantUML
A sequence diagram contains a sequence of interactions. 
An interaction is either a pair of a request and a response or it is an alternative. 
The alternative, in turn, contains more interactions. 

![./plantuml/model/Puml.png](./plantuml/model/Puml.png)

### Request/response pairs
A scenario contains several roundtrips, each consisting of one request and one response. 
Each request has an HTTP method, a URL, a receiver, and, if applicable, several parameters. 
A response can consist of several HTTP status codes and several data elements.

![./core/src/main/resources/metamodels/reqrespairs/RequestResponsePairs.png](./core/src/main/resources/metamodels/reqrespairs/RequestResponsePairs.png)

### REST Assured
The abstract syntax of REST Assured is very similar to the actual REST Assured syntax. 
In REST Assured, a client calls an HTTP method on a URL and sends parameters if necessary. 
The client checks whether it receives an answer and whether the received answer meets its expectations: 
The IsIn-Matcher checks whether the received status code is an expected status code. 
The HasXPath-Matcher checks whether a received data element is an expected data element. 

![./core/src/main/resources/metamodels/restassured/RestAssured.png](./core/src/main/resources/metamodels/restassured/RestAssured.png)
 
## Demo
1. Given is a PlantUML sequence diagram.
```
SEQUENCE @startuml
PARTICIPANT A
PARTICIPANT B  
A -> B : GET "/hello"  
activate B  
B -> A : 200  
deactivate B  
@enduml
 ``` 
 
2. The parser that Xtext generates for PlantUML parses the sequence diagram into its XMI representation.
```
<?xml version="1.0" encoding="UTF-8"?>
 <puml:UmlDiagram xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xmlns:puml="http://www.eclipse.plantuml/Puml">
   <umlDiagrams xsi:type="puml:SequenceUml">
     <umlElements xsi:type="puml:Participant" name="A"/>
     <umlElements xsi:type="puml:Participant" name="B"/>
     <umlElements xsi:type="puml:UseLeft" userOne="#//@umlDiagrams.0/@umlElements.0"
         userTwo="#//@umlDiagrams.0/@umlElements.1">
       <content xsi:type="puml:Request" method="GET" url="/hello"/>
     </umlElements>
     <umlElements xsi:type="puml:Activate" activate="#//@umlDiagrams.0/@umlElements.1"
         deactivate="#//@umlDiagrams.0/@umlElements.1">
       <umlElements xsi:type="puml:UseLeft" userOne="#//@umlDiagrams.0/@umlElements.1"
           userTwo="#//@umlDiagrams.0/@umlElements.0">
         <content xsi:type="puml:Response">
          <code>200</code>
         </content>
       </umlElements>
     </umlElements>
   </umlDiagrams>
 </puml:UmlDiagram>
 ```

3. QVTO transforms the XMI sequence diagram into request/response pairs.
```
<?xml version="1.0" encoding="UTF-8"?>
 <RequestResponsePairs:Scenario xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI"
     xmlns:RequestResponsePairs="http://www.example.org/RequestResponsePairs" scenarioName="minimal_hello_puml">
   <roundtrip roundtripName="roundtrip1">
     <httprequest httpMethod="GET" url="/hello" receiver="B"/>
     <httpresponse>
       <httpStatus>200</httpStatus>
     </httpresponse>
   </roundtrip>
 </RequestResponsePairs:Scenario>
```
4. QVTO transforms the request/response pairs into the abstract syntax of REST Assured.
```
<?xml version="1.0" encoding="UTF-8"?>
 <RestAssured:TestScenario xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xmlns:RestAssured="http://www.example.org/RestAssured" testScenarioName="minimal_hello_puml">
   <testroundtrip testRoundtripName="roundtrip1">
     <requestspecification method="GET" url="/hello" receiver="B"/>
     <responsespecification>
       <bodymatchergroup/>
       <statusmatcher xsi:type="RestAssured:IsIn">
         <value>200</value>
       </statusmatcher>
     </responsespecification>
   </testroundtrip>
 </RestAssured:TestScenario>
```

5. Acceleo generates Java test cases from the abstract syntax of REST Assured.
```
public void test() throws Exception {
    try {
 		Response roundtrip1 = RestAssured.given()
 				.auth().basic(substitutor.replace("${B.username}"), substitutor.replace("${B.password}"))
 			.when()
 				.get(substitutor.replace("${B.path}") + substitutor.replace("/hello"))
 			.then()
 				.assertThat()
 					   .statusCode(IsIn.isIn(Arrays.asList(200)));
 	} catch (Exception exception) {
 		System.out.println("An error occured during evaluating the communication with testReceiver: ");
 		exception.printStackTrace();
 		throw exception;
 	}
}
```

## Installation
1. Install Java SE Development Kit 8 or higher. 
You can find Java SE Development Kit 8 under the website [https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
2. Clone the Plantestic repository.
3. Run `./gradlew build`.

## How to use
### Input requirements
The input is a PlantUML sequence diagram. 
This sequence diagram contains several participants and interactions between the participants. 
One participiant is the client who calls the test cases. The other participants are services of the implementation. 
In the example diagram, the client is `CCC` and the services are `CRS` and `Voicemanager`.

An interaction contains a request from the client and a response from a service. 
A request contains an HTTP method, a URL, and possibly parameters. 
A response contains an HTTP status code and, if applicable, data. A hyphen separates the HTTP status codes and the data. 

The HTTP method is `GET`, `POST`, or `PUT`. 

The URL path is a String. In it, slashes separate the path components. 
A parameter name in curly braces, preceded by a dollar symbol, thereby specifies parameterized path components. 
Example: ```/path/${param}```

We specify the request parameters in a tuple: 
An opening bracket is followed by the first request parameter. 
This request parameter is followed - comma-separated - by the second request parameter, and so on. 
The last request parameter is followed by a closing bracket.
We specify the request parameter as a `Name: Value` pair: 
The name of the request parameter is followed by a space, a colon, a space, and the value of the request parameter as a string. 
We define the value of the request parameter in curly brackets, preceded by a dollar symbol. 
Example: ```(name1 : "${value1}", name2 : "${value2}")```

We specify the response data in a tuple: 
An opening bracket is followed by the first response datum. 
This response datum is followed - comma-separated - by the second response datum, and so on. 
The last response datum is followed by a closing bracket.
We specify the response datum as a `Name: XPath` pair: 
The name of the response datum is followed by a space, a colon, a space, and the xpath of the response datum as a string. 
In the xpath, slashes separate the path components. . 
Example: ```(name1 : "/value/value1", name2 : "/value2")```

![./core/src/test/resources/rerouting.png](./core/src/test/resources/rerouting.png)

### Execution
1. Create a PlantUML sequence diagram. Note the input requirements above. 
2. Save the sequence diagram. 
3. Call the command `./gradlew run --args="<path/to/sequence/diagram/diagram_name.puml>"`.

### Output expectation
The generated test cases are in `<path/to/plantestic_folder/core/build/resources/main/code-generation/generatedCode/<diagramName>.java>`.

## Credits
### Contributors 
- [Stefan Grafberger](https://github.com/stefan-grafberger) *
- [Fiona Guerin](https://github.com/FionaGuerin) *
- [Michelle Martin](https://github.com/MichelleMar) *
- [Daniela Neupert](https://github.com/danielaneupert) *
- [Andreas Zimmerer](https://github.com/Jibbow) *

\* contributed equally

### Repositories
#### plantuml-eclipse-xtext
The repository [plantuml-eclipse-xtext](https://github.com/Cooperate-Project/plantuml-eclipse-xtext) defines the grammar of PlantUML. 
We pass this grammar to Xtext.
   
#### qvto-cli
The repository [qvto-cli](https://github.com/mrcalvin/qvto-cli) demonstrates how QVT operations can be performed without Eclipse.
   
### Literature
#### Standalone Parsing with Xtext
From the article [Standalone Parsing with Xtext](http://www.davehofmann.de/different-ways-of-parsing-with-xtext/) we learned how to use Xtext without Eclipse.
    
#### QVTOML/Examples/InvokeInJava
From the Wiki article [QVTOML/Examples/InvokeInJava](https://wiki.eclipse.org/QVTOML/Examples/InvokeInJava) we learned how to call QVT from our pipeline.

#### Grammar-based Program Generation Based on Model Finding
From the paper [Grammar-based Program Generation Based on Model Finding](http://www.informatik.uni-bremen.de/agra/doc/konf/13_idt_program_generation.pdf) we learned about the Eclipse Modeling Framework.
   
## License
Copyright [2019] [Stefan Grafberger, Fiona Guerin, Michelle Martin, Daniela Neupert, Andreas Zimmerer]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
