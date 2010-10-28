#!/usr/local/bin/python3.1
# -*- coding: UTF-8 -*- 
# filename   : ssf2jubilee.py
# author     : Jinho D. Choi
# last update: 10/6/2010
import sys
import glob
from ssf_api import *

SSF_DIR  = sys.argv[1]	# directory path containing SSF files
SSF_EXT  = sys.argv[2]	# SSF file extension (e.g., 'prun', 'dat')
TREE_DIR = sys.argv[3]
TASK_DIR = sys.argv[4]
f_map    = open('frameset_map.txt')
d_map    = dict()

def makeThreeDigits(i):
	if   i < 10 : return '00'+str(i)
	elif i < 100: return '0' +str(i)
	else        : return str(i)

def getPof(tree, drel, headId):
	for chunk in tree:
		if chunk.isChild(drel, headId):
			return str(chunk[1].tokenId)+':1-ARGM-PRX'

	return None

# retrieve lemma map
for line in f_map:
	ls = line.split()
	d_map[ls[0]] = ls[1]

ls_prop = list()

for ssfFile in glob.glob(SSF_DIR+'/*.'+SSF_EXT):
	prefix   = ssfFile[ssfFile.rfind('/')+1:ssfFile.rfind('.')]
	treeFile = prefix + '.parse'
	ssf      = SSF(ssfFile)
	fTree    = open(TREE_DIR+'/'+treeFile, 'w')
	print(ssfFile)
	
	for treeId, tree in enumerate(ssf.getTrees()):
		lTree = ['((SSF']
		tree.generateTokenIDs()
		
		for chunk in tree:
			lTree.append('    '+chunk.toJubilee())
			
			if chunk.isVerb():
				lProp  = [treeFile, str(treeId)]
				headId = chunk.getName()
		
				for node in chunk[1:]:
					if node.posEquals('VM'):
						predId = str(node.tokenId)
						lemma  = node.getLemma()
						
						if lemma in d_map:
							lemma = d_map[lemma]
						else:
							continue
						
						lProp.append(predId)
						lProp.append('userId')
						lProp.append(lemma + '-v')
						lProp.append(lemma + '.XX')
						lProp.append('-----')
						lProp.append(predId+':0-rel')
						
						prx = getPof(tree, 'pof', headId)
						if prx: lProp.append(prx)
						
						ls_prop.append((lemma, lProp))
						break
	
		lTree.append('))\n')
		fTree.write('\n'.join(lTree))

	fTree.close()

ls_prop.sort()
index = 1
for i,(lemma, lProp) in enumerate(ls_prop):
	if i%100 == 0:
		fTask = open(TASK_DIR+'/'+makeThreeDigits(index)+'.task', 'w')
		index += 1

	fTask.write(' '.join(lProp)+'\n')



