
from random import seed, choice, randint, shuffle
from itertools import product
from collections import deque
import re

seed(1337)

class TestCase:
   probname = 'flow'
   dataCount = 0
   def __init__(self, suiteID, numNodes, source, sink, edges):
      self.numNodes = numNodes
      self.source = source
      self.sink = sink
      shuffle(edges)
      self.edges = edges
      header = 'data/in/'
      testID = TestCase.dataCount
      TestCase.dataCount += 1
      header += TestCase.probname + '-'
      self.filename = header + ('%02d-%04d' % (suiteID, testID)) + '.in'

   def write(self):
      print(self.filename)
      file = open(self.filename, "w")
      file.write('%d %d %d %d\n' % (self.numNodes, len(self.edges), self.source, self.sink))
      for a, b, w in self.edges:
         file.write('%d %d %d\n' % (a,b,w))
      file.close()


edges = [(1,2,1), (1,3,1), (2,5,1), (2,4,1), 
         (3,5,1), (4,6,1), (5,6,1)]
TestCase(0, 6, 1, 6, edges).write() 


def randomEdgeCap((lo, hi)):
  return randint(lo, hi)

# Generates fully connected bipartite graphs with given edge weights
def generateBipartiteSuite(suiteId, numTests, leftSize, rightSize, capRange):
   for curTest in range(numTests):
      leftVertices = range(1, leftSize+1)
      rightVertices = range(leftSize+1, leftSize+1+rightSize)
      source = len(leftVertices) + len(rightVertices) + 1
      sink = source + 1
      numVertices = sink

      edges = [(i, j, randomEdgeCap(capRange)) for i, j in product(leftVertices, rightVertices)]

      count = [0] * numVertices
      for i, j, cap in edges:
         count[i] += cap
         count[j] += cap

      edges += [(source, i, randint(1, count[i])) for i in leftVertices]
      edges += [(j, sink, randint(1, count[j])) for j in rightVertices]
      TestCase(suiteId, numVertices, source, sink, edges).write()


def genKRandomChoices(k, choices):
   sz = len(choices)
   result = set()
   while len(result) < k:
      v = choices[randint(0, sz-1)]
      result.add(v)
   return list(result)

def generateBPMSuite(suiteId, numTests, leftSize, rightSize, degree):
   numVertices = leftSize+rightSize+2

   for curTest in range(numTests):
      leftVertices = range(1, leftSize+1)
      rightVertices = range(leftSize+1, leftSize+1+rightSize)
      source = len(leftVertices) + len(rightVertices) + 1
      sink = source + 1
      numVertices = sink

      edges = []
      for i in leftVertices:
         d = randint(1, degree)
         matches = genKRandomChoices(d, rightVertices)
         edges += [(i, j, 1) for j in matches]
      edges += [(source, i, 1) for i in leftVertices]
      edges += [(j, sink, 1) for j in rightVertices]
      TestCase(suiteId, numVertices, source, sink, edges).write()


def generateCapBPMSuite(suiteId, numTests, leftSize, rightSize, degree, capRange):
   numVertices = leftSize+rightSize+2

   for curTest in range(numTests):
      leftVertices = range(1, leftSize+1)
      rightVertices = range(leftSize+1, leftSize+1+rightSize)
      source = len(leftVertices) + len(rightVertices) + 1
      sink = source + 1
      numVertices = sink

      edges = []
      for i in leftVertices:
         d = randint(1, degree)
         matches = genKRandomChoices(d, rightVertices)
         edges += [(i, j, randomEdgeCap(capRange)) for j in matches]

      count = [1] * numVertices
      for i, j, cap in edges:
         count[i] += cap
         count[j] += cap

      edges += [(source, i, randint(1, count[i])) for i in leftVertices]
      edges += [(j, sink, randint(1, count[j])) for j in rightVertices]
      TestCase(suiteId, numVertices, source, sink, edges).write()


def generateRandomGridSuite(suiteId, numTests, size, capRange):
   numVertices = size * size

   deltas = [(-1,0), (1,0), (0,-1), (0,1)]
   def toVert(i, j):
      return size * i + j + 1

   for curTest in range(numTests):
      
      cells = list(product(range(size), range(size)))
      source = toVert(*choice(cells))
      sink = toVert(*choice(cells))
      edges = []
      for i, j in cells:
         x = toVert(i, j)
         for di, dj in deltas:
            c2 = (i+di, j+dj)
            if 0 <= c2[0] and c2[0] < size and 0 <= c2[1] and c2[1] < size:
               y = toVert(*c2)
               edges.append((x, y, randomEdgeCap(capRange)))
      
      TestCase(suiteId, numVertices, source, sink, edges).write()


def generateLevelSuite(suiteId, numTests, levelSize, numLevels, capRange):
   numVertices = levelSize * numLevels + 2

   for curTest in range(numTests):

      levelVertices = []
      curPos = 1
      for x in range(numLevels):
         level = range(curPos, curPos + levelSize)
         levelVertices.append(level) 
         curPos += levelSize

      source = curPos
      sink = source + 1
      numVertices = sink

      edges = []
      for x in range(1, numLevels):
         levelPairs = product(levelVertices[x-1], levelVertices[x])
         edges += [(i, j, randomEdgeCap(capRange)) for i, j in levelPairs]
         
      count = [0] * numVertices
      for i, j, cap in edges:
         count[i] += cap
         count[j] += cap

      for i in levelVertices[0]:
         edges.append((source, i, randint(1, count[i])))
      for i in levelVertices[-1]:
         edges.append((i, sink, randint(1, count[i])))

      TestCase(suiteId, numVertices, source, sink, edges).write()


generateBipartiteSuite(1, 10, 50, 50, (1, 1000))

from math import sqrt
generateBPMSuite(2, 5, 50-1, 50-1, int(sqrt(50+50)))
generateBPMSuite(3, 5, 50 - 1, 50 - 1, 10)
generateCapBPMSuite(4, 5, 50 - 1, 50 - 1, 10, (1, 10000))
generateRandomGridSuite(5, 5, 50, (1, 10 ** 4))
generateLevelSuite(6, 5, 20, 10, (1, 1000))
