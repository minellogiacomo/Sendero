
---------------
sendero project
---------------
---------------

wiki.bath.ac.uk/display/sendero



1. Python scripts for manipulating sendero output
-------------------------------------------------

sendero simulations, especially multiple runs in batch mode, can produce very
large volumes of output.

Two python scripts are included, which we have used to summarise and produce
graphs from the raw output data files. The scripts are:

nkprocess.py
nkcprocess.py

Each script produces three comma-separated value files for import into
statistical analysis packages, as well as graph output.

2. Installing the scripts
-------------------------

These scripts require Python 2.4 or newer to run, and depend on the matplotlib
and numpy Python libraries.

Instructions for installing numpy and matplotlib on Windows, Mac OS X and Linux
can be found at:

http://matplotlib.sourceforge.net/installing.html

Both of the python scripts produce graphs in .png format. This requires
configuring matplotlib's backends to use the "Agg" backend. To output other
graph files, the python scripts should be changed to append the correct file
extension as required (matching the chosen backend file type).

see http://matplotlib.sourceforge.net/backends.html for more information.

3. Running the scripts
----------------------

Output files from batch runs or single runs (in GUI mode) can both be processed
by the scripts. In the case of multiple runs, values will be averaged (and
shorter runs will be extrapolated to the length of the longest run, repeating
the values at their final tick).

To run the scripts, from a command prompt type:

python nk(c)process.py -f outputfile.txt

where outputfile.txt is a sendero output file.

CSV files and graphs will be output in the directory from where the script was
run.
