# Model-driven Software Engineering
This test case generator produces test cases from a sequence diagram. 
A sequence diagram models a sequence of interactions between objects. 
A test case then checks for such an interaction whether it is implemented as the sequence diagram defines it. 
In an example sequence diagram called `Hello`, let there be two actors Alice and Bob. 
Alice sends Bob the request `GET /hello ` and Bob answers with `Hello World`. 
The corresponding test case now sends an HTTP request `GET /hello` to the backend. 
The test case then expects a response with status `200 OK` and date `Hello World`.

# Motivation
The implementation of user requirements often deviates from the specification of the same user requirements. 
Individual work, teamwork, and collaboration between teams can produce such a divergence. 
For example, requirements may be misinterpreted or overlooked. 
Teamwork, especially with multiple teams, causes interface errors. 
For example, subsystems of the same product may use conflicting technologies or conflicting data formats.

Our test case generator detects deviations at an early stage: 
The test case generator derives test cases directly from the specification. 
If the implementation fulfills these test cases, then the implementation fulfills the specification. 
If the implementation does not fulfill these test cases, the implementation deviates from the specification. 
With our test case generator, developers can quickly uncover inconsistencies, fix them, and save costs.

# Conventions
## Repository language
English

## Review process
A pull request addresses a single issue. 

A pull request must be approved by two reviewers.

A pull request is squashed before the merge.

## Naming Conventions: Issues, branches, pull requests, squashed merge commits
Issues describe project work such as tasks, bugs, and feature requests. 
Example: ```Implement task XYZ```

The branch name follows the issue name. 
The automatically generated issue ID is succeeded by the issue name in lowercase letters, with hyphens separating multiple words.
Example: ```#1-implement-task-xyz```

The pull request name is the issue name plus the issue id.
Example: ```Implement task XYZ (Issue #1)```

The squashed merge commit name is the issue name plus the issue id plus the pull request id. 
Example: ```Implement task XYZ (Closes #1) (PR #2)```

If a pull request addresses several issues, the pull request name is a summary of the issues plus the issue ids.
The squashed merge commit name is a summary of the issues plus the issue ids plus the pull request id.
Example pull request: ```Initial Setup (Issue #1, Issue #2, Issue #3)```
Example squashed merge commit: ```Initial Setup (Closes #1, Closes #2, Closes #3) (PR #2)```

# Frameworks, libraries, standards
## Xtext for parsing PlantUML files
We use [Xtext](https://www.eclipse.org/Xtext/) to generate the parser and the corresponding language from a grammar to read the DSL of PlantUML.

To generate the language, you can run
```
./gradlew generateXtext
```
The generated meta-model files are located in `plantuml/model/generated/Puml.ecore` and `plantuml/model/generated/Puml.genmodel`.
The language and the parser are located in `plantuml/src/main/xtext-gen` and `plantuml/src/main/xtend-gen`.
This gradle task is also automatically executed when you build the project.

## Eclipse Modelling Framework (EMF)
The Eclipse Modeling Framework serves modeling and code generation. 
From a model specification in XMI it produces an equivalent set of classes in Kotlin.
Website: [https://www.eclipse.org/modeling/emf/](https://www.eclipse.org/modeling/emf/)

## PlantUML
The open source tool PlantUML produces a UML diagram from simple text language. 
Such a PlantUML diagram can be a sequence diagram, use case diagram, class diagram, activity diagram, component diagram, 
state diagram, object diagram, deployment diagram, or timing diagram.
Website: [http://plantuml.com](http://plantuml.com)

## REST-assured
The Rest-assured library simplifies the verification and validation of REST APIs. 
As such, the test techniques of Rest-assured follow the test techniques of dynamic languages such as Ruby and Groovy. 
The library offers all standardized HTTP operations.
Website: [http://rest-assured.io](http://rest-assured.io)

## Cucumber
The Cucumber tool tests the behavior of software. 
The test case that tests this behavior is defined in natural language. 
For this, Cucumber uses the plain language parser Gherkin.
Website: [https://cucumber.io](https://cucumber.io)

## XML Metadata Interchange (XMI)
XML Metadata Interchange is an exchange format among software development tools. 
For example, XML Metadata Interchange represents UML diagrams textually within the Eclipse Modeling Framework.
Website: [https://www.omg.org/spec/XMI/About-XMI/](https://www.omg.org/spec/XMI/About-XMI/)

# Credits
## Contributors
- [Stefan Grafberger](https://github.com/stefan-grafberger)
- [Fiona Guerin](https://github.com/FionaGuerin)
- [Michelle Martin](https://github.com/MichelleMar)
- [Daniela Neupert](https://github.com/danielaneupert)
- [Andreas Zimmerer](https://github.com/Jibbow)

## Repositories
### PlantUML-Eclipse-Xtext
[https://github.com/Cooperate-Project/plantuml-eclipse-xtext](https://github.com/Cooperate-Project/plantuml-eclipse-xtext)
   developed a grammar file for PlantUML.
   We built upon their work from their repository and marked their contributions in the repository.
   
### QVTO-CLI
[https://github.com/mrcalvin/qvto-cli](https://github.com/mrcalvin/qvto-cli)
    gave us a lot of insights in how to run `qvt` without Eclipse.
   
## Articles
### Standalone Parsing with Xtext
[http://www.davehofmann.de/different-ways-of-parsing-with-xtext/](http://www.davehofmann.de/different-ways-of-parsing-with-xtext/)
   wrote a very useful article on how to use an Xtext parser in standalone mode.
    
### QVTOML/Examples/InvokeInJava
[https://wiki.eclipse.org/QVTOML/Examples/InvokeInJava](https://wiki.eclipse.org/QVTOML/Examples/InvokeInJava)
   shows how to invoke `qvt` programmatically.
   
# License
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
