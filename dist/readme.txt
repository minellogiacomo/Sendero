
---------------
sendero project
---------------
---------------


Running sendero
---------------

Sendero requires Java JRE 1.5 (or later) to run.

To run the project from the command line type the following:

java -jar sendero.jar (run type) (param file)

where (run type) is one of:

nkbatch		NK model batch mode
nkgui		NK model with Repast GUI
nkcbatch	NKC model batch mode
nkcgui		NKC model with Repast GUI

and (param file) is the approriate parameter file:

NKdefaultparams.pf	for either NK model run type
NKCdefaultparams.pf	for either NKC model run type

For example, an NK model run in GUI mode using the supplied parameter file would
be run by typing (at the command line):

java -jar sendero.jar nkgui NKdefaultparams.pf

NOTE for NKC runs, an XML file is required (and is supplied). The location of
this file is referred to in NKCdefault_params.pf.
For more informtion on running sendero, and the parameter files for NK and NKC
models, see the wiki at:

http://wiki.bath.ac.uk/display/sendero

IMPORTANT sendero runs can require large amounts of RAM. 

In the event of the program crashing with the error "java.lang.OutOfMemoryError: 
Java Heap Space", or if performance is poor, try increasing the memory
available to the Java Virtual Machine (JVM).

Information on JVM memory tuning is given at:

http://java.sun.com/javase/technologies/hotspot/vmoptions.jsp
