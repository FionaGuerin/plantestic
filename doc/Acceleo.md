# Acceleo

## General facts

- Open Source, part of Eclipse Foundation
- For template-based source code generation (model-to-text transformation)
- Independent from Eclipse environment, but deeply integrated within Eclipse IDE (syntax highlighting, real time error detection, ... -> has its own perspective within Eclipse)
- Implementation of MOFM2T standard of the OMG

## Prerequisites

 - Java JDK >= 1.5
 - Acceleo >= 3.3
 - needs a Metamodel for the selecting the types of the generation module
 - (Eclipse >= Luna)
 - (UML2 installed into Eclipse)
 
## How Acceleo transforms the abstract syntax in REST Assured to Java code
 
 - The definition of the code generation description is within the _generate.mtl_ file  
   This file is structured as follows:
   ```
    // some encoding information 
    [comment encoding = UTF-8 /]
	// this defines which model is taken, list of all URIs
    [module generate('http://www.eclipse.org/uml2/5.0.0/UML')]
	
	// templates are the description of Element to generate
	[template public generateElement(m : Model)] 
      [comment @main /]
      [file (m.name, false)]
        //TODO implement stuff
      [/file]
    [/template]
	```
 - 
 
 ## Language Features
 
 [Acceleo Query Language](https://www.eclipse.org/acceleo/documentation/)
 
 - It is a Markup language with open and closing tags, which are in square brackets  
   not enclosed text is directly added to the generated file
 - Relevant tags:
   - [template public generateElement(m : Model)]  
     general template, is generated alone
   - [for (element) separator('xy')] doSomeStuff [/for]  
     Iteration over the element specification, repeat doSomeStuff
   - [if  (condition)] doSomeStuff [/if]  
     If condition is true, doSomeStuff
   - [let name : type = value]  
     temporary value assignment
 
 Some project to look for ideas: <https://gist.github.com/aranega/f07e4cb4e850af3288af>

## Import Acceleo into Eclipse workflow
 
 1. Add Acceleo character to project  
   -> has to be done within eclipse  
   -> adds eclipse/acceleo specific files (`build.properties`, `.do_not_generate`, `.project`) 
 2. add `mtl` file  
   -> contains the acceleo code from which the Code is generated
 3. set _Run Configuration_  
   -> _Project_: Defines for which project the run configuration is to be applied  
   -> _Main class_: Defines where the main class is found to generate the code  
   -> _Model_: Defines the location of the model that should be tranformed into code  
   -> _Target_: Defines the location for the generated code
 