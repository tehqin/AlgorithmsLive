
import os
import sys
import glob
import time
import string
import subprocess

generatingProgram = 'B'
outputFilenamePrefix = ''
outputSuffix = '.a'

prefixPattern = 'data\\'
inputPattern = prefixPattern + 'in\\*'
outputDirectory = prefixPattern + 'out2\\'
inputFiles = sorted(glob.glob(inputPattern))

print 'Compiling...'
cmd = ['javac', generatingProgram+'.java']
try:
   os.system(' '.join(cmd))
except OSError:
   print 'Compile Failed!'
   exit(0)
print 'Done!'

def getTestNumber(myFilename):
   for i in range(len(myFilename)):
      if myFilename[i:].isdigit():
         return myFilename[i:]
   print 'Error! input data not in format required!'
   exit(0)

maxTime = 0
jvmPrefix = ['java', '-Xmx2048M', '-Xss32M', generatingProgram]
for inFile in inputFiles:
   print 'Running input', inFile
   outFile = outputDirectory + outputFilenamePrefix + getTestNumber(inFile) + outputSuffix
   print 'Generating output', outFile
   io = ['<', inFile, '>', outFile]
   cmd = jvmPrefix+io

   start = time.time()
   os.system(' '.join(cmd))
   elapsed = time.time()-start
   print 'Elapsed:', elapsed
   maxTime = max(maxTime, elapsed)

print 'Worse Case Runtime:', maxTime
