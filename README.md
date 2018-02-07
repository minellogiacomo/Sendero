# Sendero NKCS [![HitCount](http://hits.dwyl.io/MGrizzly/Sendero.svg)](http://hits.dwyl.io/MGrizzly/Sendero)

Sendero has implemented Stuart Kauffman's NK(C) models in REPAST, an agent-based modeling environment.
Further details of the NK(C) models are provided in the University of Bath Working Paper ["Sendero: an extended, agent-based implementation of Kauffman's NKCS model"](https://wiki.bath.ac.uk/download/attachments/15500198/NKCS.pdf?version=1&modificationDate=1241178853000&api=v2).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and research purposes.

First clone this repo or download a zip copy of the files. 

## Built With

* [JDK 1.5+](http://www.oracle.com/technetwork/java/javase/downloads/index.html) - Java Development Kit
* [Ant](http://ant.apache.org/) - Build
* [Repast](https://repast.github.io/) - Recursive Porus Agent Simulation Toolkit

### Prerequisites

Dependencies are embedd in the repository. Just be sure to have the build tools installed in your system.

### Build Sendero

The application can be built using Apache Ant and the supplied script "build.xml". Navigate to "dist" folder and type 

```ant```

at the command line. 

## Tests

Tests are currently under development.

## Running Sendero

To run the project from the command line, go to the dist folder and type the following:

```java -jar sendero.jar (run type) (param file)```

For the NK model with Repast GUI:

```java -jar sendero.jar nkgui NKdefaultparams.pf```

For the NK model in batch mode:

```java -jar sendero.jar nkbatch NKdefaultparams.pf```

For the NKC model with Repast GUI:

```java -jar sendero.jar nkcgui NKCdefaultparams.pf```

For the NKC model in batch mode:

```java -jar sendero.jar nkcbatch NKCdefaultparams.pf```

NOTE: for NKC runs, an XML file is required (and is supplied). The location of this file is referred to in NKCdefault_params.pf.

## Contributing [![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/dwyl/esta/issues)

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

* **Julian Padget** 
* **Richard Vidgen** 
* **Amy Marshall** 
* **Rick Mellor** 
* **James Mitchell** 

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## Maintainers

* **Giacomo Minello** [@JackMinello](https://twitter.com/JackMinello) 

## Old Wiki

The original code and wiki can be foud [here](https://wiki.bath.ac.uk/display/sendero/NKC).

## License [![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE.md](LICENSE.md) file for details

## References

Kauffman, S., (1993). The Origins of Order. Oxford.

Kauffman, S., (1995). At Home in the Universe. Penguin Books edition, 1996.

McKelvey, B., (1999). Avoiding Complexity Catastrophe in Coevolutionary Pockets: Strategies for Rugged Landscapes. Organization Science, 10(3): 294-321. 





