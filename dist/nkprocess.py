import csv
from numpy import *
import Gnuplot
import time
from optparse import OptionParser

from pylab import *


# set up and read command line options


parser = OptionParser()
parser.add_option("-f", "--file", dest="in_filenames",
                  help="read data from FILE - enclose comma-separated file list in quotes e.g. \"FILE1, FILE2\"", metavar="FILE")
		  

(options, args) = parser.parse_args()



# set up constants

# column titles
columnvar_titles = (["A", "N", "K"])
series_titles = (["Average Fitness", "Maximum Fitness","Minimum Fitness",
		"Average Wait Before Move","Maximum Wait Before Move",
		"Minimum Wait Before Move","Average Number of Fitter Neighbours",
		"Maximum Number of Fitter Neighbours",
		"Minimum Number of Fitter Neighbours"])
		

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
	#print readstring
	#print reader.line_num

#print options_dict
#print len(options_dict)

#print options_dict['Fitness_method']

# after the model parameters, we have blank line(s) before the data headers
# keep skipping blanks, then grab the first non-blank line
# then read the first line into a list of strings.
while (1):
	readstring = reader.next()
	if len(readstring) > 0:
		column_headers = readstring;
		break
		
#print column_headers

# need to check if we have a 'run' column
# single run gui output doesn't produce one, so need to add to column headers
# First six cols are "run, tick, A_size_of, RNGseed, [N & K]_size_of"
# "tick is already contained in the data, but is overwritten with K_size_of
# as it needs to be moved
# set add_run_data -  flag to insert corresponding columns into the numeric data
add_run_data = 0
if column_headers[0]!="run":
	column_headers[0] = "K_size_of"
	column_headers = ["run", "tick", "A_size_of", "RngSeed", "N_size_of"] + column_headers
	add_run_data = 1
	print "Processing one run GUI output format..."
else:
	print "Processing batch mode output..."

#print column_headers
	
# read lines from the data until we hit a blank.
# if data is numeric, put it into out 2d list of floats
countlines=0
while (1):
	try:
		readstring=reader.next()
		countlines = countlines + 1
		if len(readstring)==0:
			print "Stopped reading"
			break
		try:
			floats.append(map(float, readstring))
		except:
			print "Bad data - not adding"
	except StopIteration:
		#print "Read:", countlines
		break

print "Read", countlines, "lines of data from file"		
		
#print floats		
xdata=array(floats)
#print xdata
# if we needed to add column headers before, we are dealing with single run
# output. If so, we need to add columns at the left of the data. 
# First six cols are "run, tick, A_size_of, RNGseed, [N & K]_size_of"
# We add five (not six) cols, since "tick" is currently in the data already
# Other values are run = 1 (by def), RNGSeed (doesn't matter)
# A, N, K values are taken from the dictionary made from the header data in the file

if add_run_data == 1:
	newcol = ones((xdata.shape[0],5), dtype="float")
	xdata = concatenate((newcol, xdata), axis=1)
	A_val = float(options_dict['A_size_of'])
	N_val = float(options_dict['N_size_of'])
	K_val = float(options_dict['K_size_of'])
	#print A_val, N_val, K_val
	xdata[:,1] = xdata[:,5] # ticks - already there but needs to move
	xdata[:,2] = A_val
	xdata[:,3] = 0 #RNG seed doesn't matter
	xdata[:,4] = N_val
	xdata[:,5] = K_val #overwrites original tick column
	#print column_headers
	#print xdata[1]
	
	

A_uniques = unique(xdata[:,2])
N_uniques = unique(xdata[:,4])
K_uniques = unique(xdata[:,5])

series_total = len(A_uniques) * len(N_uniques) * len(K_uniques) 

# set up an array to hold averages (no columns for run number or rng seed)
# needs to move to handle multiple variables
maxticks = xdata[:,1].max()
#print series_total
#print maxticks
#print xdata.shape[1]-2
averages = zeros((series_total, maxticks, xdata.shape[1]-2), float)

# three loop setup for varying A / N / K values

series_counter = 0
Aseries_name=""
Nseries_name=""
Kseries_name=""
series_keys=[]

for A_value in A_uniques:
	if len(A_uniques)>1:
		Aseries_name = "A=" + str(A_value) + ", "
	dataA = compress(xdata[:,2]==A_value, xdata, axis=0)
	
	for N_value in N_uniques:
		#if len(N_uniques)>1:
		Nseries_name = "N=" + str(N_value) 
		dataAN = compress(dataA[:,4]==N_value, dataA, axis = 0)
		
		for K_value in K_uniques:
			#if len(K_uniques)>1:
			Kseries_name = ", " + "K=" + str(K_value)
				
			dataANK = compress(dataAN[:,5]==K_value, dataAN, axis = 0)
				
			series_keys.append(Aseries_name + Nseries_name + Kseries_name)
			
			
			
			# when multiple variables are used, run values continue to count from the
			# previous variable value (e.g. A=2 (runs 1-100) A=3 (runs 101-200))
			# we need to number the runs in ascending order from 1.
			firstrun=dataANK[:,0].min()
			lastrun=dataANK[:,0].max()
			totalruns = 1 + lastrun - firstrun
			
			#print firstrun, lastrun, totalruns
			
			# for each run, find the last actual tick data
			last_tick_array = zeros((totalruns, dataANK.shape[1]), float)
			#print last_tick_array.shape
			
			for k in arange(totalruns):	# for each run get the data for the last tick
				this_run=compress(dataANK[:,0]==k+firstrun, dataANK, axis=0)
				last_tick_array[k]=this_run[-1]
		
			#print "Last tick array"
			
			#print last_tick_array[-1]
			
			print "Processing simulation " + str(series_counter+1) + "/" + str(series_total)
			for i in arange(maxticks):  # for each tick value up to the maximum
				# array to hold one tick from each run for averaging
				# will contain either actual or extrapolated data
				selected_ticks = zeros((totalruns, dataANK.shape[1]), float)
				#print selected_ticks
				# get dataANK for this tick from all runs. May be empty.
				this_tick = compress(dataANK[:,1]==i+1, dataANK, axis=0)
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
				averages[series_counter][i][0]=i+1	# tick number
				averages[series_counter][i][1]=selected_ticks[:,2].max() #A_size_of
				averages[series_counter][i][2]=selected_ticks[:,4].min() #N_size_of
				averages[series_counter][i][3]=selected_ticks[:,5].min() #K_size_of
				for m in xrange(6,16):
					#print m
					averages[series_counter][i][m-2]=selected_ticks[:,m].mean()
					
				
			# increment to fill next index
			series_counter = series_counter + 1

# matplotlib plots

print "Plotting graphs..."

matplotlib.use('Agg')

for graph_num in (4,5,6,7,8,9,10,11,12,13):
	ylabel(column_headers[graph_num+2])
	
	
	
	for ser in range(series_total):
		plot(averages[ser][:,0], averages[ser][:,graph_num], label=series_keys[ser])
		
	legend(loc='center right').draw_frame(0)
	show()
	savefig('nk' + column_headers[graph_num+2] + '.png')
	clf()
		

print "Writing CSV files"

f1 = open("nk_allticks.csv","wt")
csv1 = csv.writer(f1)

f2 = open("nk_finaltick.csv","wt")
csv2 = csv.writer(f2)

column_headers.remove('RngSeed')

# replace spaces for underscores in column headers for better file compatability
for i in range(len(column_headers)):
	column_headers[i]=column_headers[i].replace(' ','_')


try:
	csv1.writerow(column_headers[1:])
	csv2.writerow(column_headers[1:])
	for series in range(series_total):
		csv1.writerows(averages[series])
		csv2.writerow(averages[series][-1])
finally:
	f1.close
	f2.close
	
f3 = open("nk_allticks_crosstab.csv","wt")
csv3 = csv.writer(f3)

out_array = zeros((averages[0].shape[0], 1 +(series_total * 10)), float)
out_array[:,0] = averages[0][:,0]

headers=[1000]#[8 * series_total]
headers[0]="tick"

datacol = 1
for column_num in (3, 4,5,6,7,8,9,10,11,12):
	for ser in range (series_total):
		headers.append((column_headers[column_num+2] + " " + series_keys[ser]).replace(' ','_'))
		out_array[:,datacol] = averages[ser][:,column_num+1]
		#print averages[ser][0]
		
		datacol  = datacol + 1
		
		
#print headers
#print out_array.shape
	
try:
	csv3.writerow(headers)
	csv3.writerows(out_array)
finally:
	f3.close	
	

		
	
	


