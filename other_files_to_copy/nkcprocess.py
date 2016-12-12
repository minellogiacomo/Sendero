import csv
from numpy import *

from optparse import OptionParser

from pylab import *


# set up and read command line options


parser = OptionParser()
parser.add_option("-f", "--file", dest="in_filenames",
                  help="read data from FILE - enclose comma-separated file list in quotes e.g. \"FILE1, FILE2\"", metavar="FILE")
		  

(options, args) = parser.parse_args()

# set up constants

# column titles

graph_titles = (["Average Fitness", "Maximum Fitness","Minimum Fitness",
		"Average Wait Before Move","Maximum Wait Before Move",
		"Minimum Wait Before Move","Average Number of Fitter Neighbours",
		"Maximum Number of Fitter Neighbours",
		"Minimum Number of Fitter Neighbours",
		"Fraction Still Walking"])
		
#print "We have", len(graph_titles), " graph titles"		
	

f = open(options.in_filenames)
reader = csv.reader(f)
floats = []
options_dict={}


# start from the first line in the file
# read lines until we hit a blank
# lines will be in form "Key: Value"
# so split them and build a dictionary
while (1):
	readstring = reader.next()
	if len(readstring)==0:
		break
	dict_entry = readstring[0].split(': ', 1)
	options_dict[dict_entry[0]] = dict_entry[1]
	

species_count = int(float(options_dict['S_species']))
#print "Number of species = " , species_count

data_cols_count = ((len(graph_titles) -1) * species_count)+1	
#print "Data columns count is", data_cols_count

# after the model parameters, we have blank line(s) before the data headers
# keep skipping blanks, then grab the first non-blank line
# then read the first line into a list of strings.
while (1):
	readstring = reader.next()
	if len(readstring) > 0:
		column_headers = readstring;
		break
		

# need to check if we have a 'run' column
# single run gui output doesn't produce one, so need to add to column headers
# later we will insert a column of '1.0' into data array
add_run_data = 0
if column_headers[0]!="run":
	column_headers = ["run"] + column_headers
	add_run_data = 1
	print "Processing one run GUI output format..."
else:
	print "Processing multi run output..."
		
		
# find the position in column_headers list of the first data column
# i.e. Average Fitness. This allows for us to cope with the differing
# file formats output by Repast in GUI and batch modes, where the former
# omits the RNGSeed column (i.e. column3).
	
data_begin_col = column_headers.index('Average Fitness 0')
	
#print "Data begins at", data_begin_col
	

		
#print column_headers

# read lines from the data until we hit a blank / or the end for single run data
# if data is numeric, put it into out 2d list of floats
countlines=0
while (1):
	try:
		readstring=reader.next()
		#print readstring
		countlines = countlines + 1
		if len(readstring)==0:
			#print "Stopped reading"
			break
		try:
			floats.append(map(float, readstring))
		except:
			print "Adding bad data to floats"
	except StopIteration:
		#print "Read:", countlines
		break

print "Read", countlines, "lines of data from file"
#print len(floats)		
xdata=array(floats)
#print xdata.shape
# if we needed to add a "run" column above, we are dealing with single run
# output. If so, we need to prepend a column of 1's to the left side of xdata

if add_run_data == 1:
	newcol = ones((xdata.shape[0],1), dtype="float")
	xdata = concatenate((newcol, xdata), axis=1)
	
	
	
#print xdata	

# set up an array to hold averages (no columns for run number or rng seed)
# needs to move to handle multiple variables
maxticks = xdata[:,1].max()
 
print "Maximum ticks in any run =",maxticks
#print "Data shape is",xdata.shape[1]
#print xdata[0]

firstrun=xdata[:,0].min()
lastrun=xdata[:,0].max()
totalruns = 1 + lastrun - firstrun

print "First run", firstrun, "Last run", lastrun, "Total runs", totalruns

# for each run, find the last actual tick data
last_tick_array = zeros((totalruns, xdata.shape[1]), float)
#print "Last tick array shape", last_tick_array.shape

for k in arange(totalruns):	# for each run get the data for the last tick
	this_run=compress(xdata[:,0]==k+firstrun, xdata, axis=0)
	last_tick_array[k]=this_run[-1]

#print "Last tick array"

#print last_tick_array

averages=zeros((maxticks, data_cols_count + 1), float)
#print "Averages shape is", averages.shape

print "Processing data..."

for i in arange(maxticks):  # for each tick value up to the maximum
	# array to hold one tick from each run for averaging
	# will contain either actual or extrapolated data
	selected_ticks = zeros((totalruns, xdata.shape[1]), float)
	#print "Selected ticks shape is", selected_ticks.shape
	#print selected_ticks
	# get dataANK for this tick from all runs. May be empty.
	this_tick = compress(xdata[:,1]==i+1, xdata, axis=0)
	#print "this tick"
	#print this_tick
	for j in arange(totalruns):	# for each run
		if (i+1) < last_tick_array[j,1]:# do we have actual data?
			#print "Using real data"
			# if so, get it
			selected_ticks[j] = compress(this_tick[:,0]==j+firstrun, this_tick, axis=0)
			#print selected_ticks
		else:
			# if not, use the last tick we do have
			#print "Using last tick"
			selected_ticks[j] = last_tick_array[j]
	#print "selected_ticks"
	#print selected_ticks[0]
	averages[i][0]=i+1	# tick number
	
	
	for m in range(int(data_cols_count)):
		#print "m", m, "going into column", 1+m-data_begin_col
		averages[i][1+m]=selected_ticks[:,m+data_begin_col].mean()
		
	#exit()	




# testing matplotlib plots

matplotlib.use('Agg')

print "Plotting graphs..."

# for all the multiple species graphs (i.e. not Fraction Still Walking)
for graph_num in range(len(graph_titles)-1):
	# label y axis with graph title
	ylabel(graph_titles[graph_num])
	
	for species in range(int(species_count)):
		plot(averages[:,0], averages[:,1 + graph_num + ((len(graph_titles)-1) * species)], label=('species ' + str(species)))
		
	legend(loc='center right').draw_frame(0)
	show()
	savefig('nkc' + graph_titles[graph_num] + '.png')
	clf()

# "Special case - Fraction Still Walking is not per species

ylabel(graph_titles[-1])

plot(averages[:,0], averages[:,-1])

legend().draw_frame(0)
show()
savefig('nkc' + graph_titles[-1] + '.png')
clf()

print "Writing CSV file output"

# output csv files - one is all ticks, last is final tick only

for i in range(len(column_headers)):
	column_headers[i]=column_headers[i].replace(' ','_')

f1 = open("nkc_allticks.csv","wt")
csv1 = csv.writer(f1)

f2 = open("nkc_finaltick.csv","wt")
csv2 = csv.writer(f2)


try:
	csv1.writerow(column_headers[1:])
	csv2.writerow(column_headers[1:])
	
	csv1.writerows(averages)
	csv2.writerow(averages[-1])
finally:
	f1.close
	f2.close
	

