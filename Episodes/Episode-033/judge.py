
import os
import sys
import glob
import time
import string
import filecmp
import subprocess

submissionFolder = 'submissions/EdmondsKarp/'
submissionFolder = 'submissions/DinitzNaive/'
submissionFolder = 'submissions/Dinitz/'
submissionFolder = 'submissions/TidalFlowOriginal/'
submissionFolder = 'submissions/DinitzOptimized/'
submissionFolder = 'submissions/TidalFlowExp/'
submissionFolder = 'submissions/TidalFlow/'
submissionFolder = 'submissions/PushRelabelGap/'
submissionFolder = 'submissions/PushRelabelHighest/'
submissionFolder = 'submissions/PushRelabelPreflow/'
submissionFolder = 'submissions/DinitzOptimized/'
submissionFolder = 'submissions/ISAP/'
submissionFolder = 'submissions/FordFulkerson/'
codeId = 'Flow'
testOut = 'test.out'

prefixPattern = 'data/'
inputPattern = prefixPattern + '*/*.in'
outputPattern = prefixPattern + '*/*.ans'
inputFiles = sorted(glob.glob(inputPattern))
outputFiles = sorted(glob.glob(outputPattern))
print inputFiles
print outputFiles

print 'Compiling...'
cmd = ['javac', submissionFolder+codeId+'.java']
try:
   os.system(' '.join(cmd))
except OSError:
   print 'Compile Failed!'
   exit(0)
print 'Done!'

maxTime = 0
overallResult = True
jvmPrefix = ['java', '-Xmx256M', '-Xss256M', '-cp',
             submissionFolder, codeId]
for inFile, outFile in zip(inputFiles, outputFiles):
   print 'Running input', inFile
   print 'Against ouput', outFile
   io = ['<', inFile, '>', testOut]
   cmd = jvmPrefix+io

   start = time.time()
   os.system(' '.join(cmd))
   elapsed = time.time()-start
   print 'Elapsed:', elapsed
   maxTime = max(maxTime, elapsed)

   result = filecmp.cmp(testOut, outFile, shallow=False)
   message = 'PASSED' if result else 'FAILED'
   overallResult &= result
   print message
   try:
      os.remove(testOut)
   except OSError:
      pass

print 'Final Result:', 'PASSED' if overallResult else 'FAILED'
print 'Worse Case Runtime:', maxTime
