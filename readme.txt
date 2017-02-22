
This is my fork of Sendero, with arbitrary N & K in the nkcs model. 

The Sendero Project is based at the University of Bath and is led by Julian Padget (Computer Science) and Richard Vidgen (Management). Sendero has implemented Stuart Kauffman's NK(C) models in REPAST, an agent-based modeling environment. 
The software was first developed by Amy Marshall and Rick Mellor, for their final year projects while computer science students at the University of Bath. The software developed by Amy has since been developed further and packaged by James Mitchell.
---------------
sendero project
---------------
---------------

wiki.bath.ac.uk/display/sendero


1. Building sendero from source
-------------------------------

sendero requires JRE 1.8 (or later).

The application can be built using Apache Ant and the supplied script
"build.xml". Install ant, navigate to this folder and type "ant" at the command
line. 


2. Running sendero
------------------

A pre-built copy of the application is included in the "dist" folder. To run the
application without compiling, the jar files in the "depends" folder must first
be copied into the "dist/lib" folder.

To run the project from the command line, go to the dist folder and
type the following:

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

java -jar sendero.jar nkcgui NKCdefaultparams.pf

NOTE for NKC runs, an XML file is required (and is supplied). The location of
this file is referred to in NKCdefault_params.pf.

For more informtion on running sendero, and the parameter files for NK and NKC
models, see the wiki at:

http://wiki.bath.ac.uk/display/sendero
