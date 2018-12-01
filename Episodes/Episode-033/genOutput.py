
import os
import sys
import glob
import time
import string
import subprocess

submissionFolder = 'submissions/EdmondsKarp/'
generatingProgram = 'Flow'
outputDirectory = 'data/out'
outputFilenamePrefix = 'flow-'
outputSuffix = '.ans'

inputPattern = 'data/in/*.in'
inputFiles = sorted(glob.glob(inputPattern))

print 'Compiling...'
cmd = ['javac', submissionFolder+generatingProgram+'.java']
try:
   os.system(' '.join(cmd))
except OSError:
   print 'Compile Failed!'
   exit(0)
print 'Done!'

def getTestNumber(myFilename):
   myFilename = myFilename[:-3]
   print myFilename
   #for i in range(len(myFilename)):
   #   if myFilename[i:].isdigit():
   #      return myFilename[i:]
   return myFilename[-7:]
   #print 'Error! input data not in format required!'
   #exit(0)

maxTime = 0
jvmPrefix = ['java', '-Xmx2048M', '-Xss32M', '-cp', 
             submissionFolder, generatingProgram]
for inFile in inputFiles:
   print 'Running input', inFile

   testNumber = getTestNumber(inFile)
   outFilename = outputFilenamePrefix + testNumber + outputSuffix
   outFile = os.path.join(outputDirectory, outFilename)
   print 'Generating output', outFile
   io = ['<', inFile, '>', outFile]
   cmd = jvmPrefix+io

   start = time.time()
   os.system(' '.join(cmd))
   elapsed = time.time()-start
   print 'Elapsed:', elapsed
   maxTime = max(maxTime, elapsed)

print 'Worse Case Runtime:', maxTime
