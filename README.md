# Model-driven Software Engineering
This test case generator produces test cases from plantUML diagrams.

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

# Naming conventions
## Issues
Schema: #xxx-lowercase-lowercase

A # is followed by the three-digit issue ID, a hyphen, and the name of the issue. 
The name consists of lowercase letters. If necessary, a hyphen separates several words from each other.

## Branches
Branches are named after issues.

# Technology and frameworks
## PlantUML
Link: www.plantuml.com

## Cucumber
Link: https://cucumber.io