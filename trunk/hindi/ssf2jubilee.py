#!/usr/local/bin/python3.1
# -*- coding: UTF-8 -*- 
# filename   : ssf2jubilee.py
# author     : Jinho D. Choi
# last update: 10/6/2010
import sys
import glob
from ssf_api import *

SSF_DIR = sys.argv[1]	# directory path containing SSF files
SSF_EXT = sys.argv[2]	# SSF file extension (e.g., 'prun', 'dat')
f_map   = open('frameset_map.txt')
d_map   = dict()

# retrieve lemma map
for line in f_map:
	ls = line.split()
	d_map[ls[0]] = ls[1]

for ssfFile in glob.glob(SSF_DIR+'/*.'+SSF_EXT):
	treeFile = ssfFile + '.tree'
	taskFile = ssfFile + '.task'
	ssf      = SSF(ssfFile)
	fTree    = open(treeFile, 'w')
	fTask    = open(taskFile, 'w')
	print(ssfFile)
	
	for treeId, tree in enumerate(ssf.getTrees()):
		lTree = ['((SSF']
		tokId = 0
		
		for chunk in tree:
			lTree.append('    '+chunk.toJubilee())
			
			if chunk.isVerb():
				lProp = [treeFile, str(treeId)]
		
				for i,node in enumerate(chunk[1:]):
					if node.posEquals('VM'):
						predId = str(tokId + i)
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
						
						fTask.write(' '.join(lProp)+'\n')
						break
			
			tokId += len(chunk) - 1
	
		lTree.append('))\n')
		fTree.write('\n'.join(lTree))

	fTree.close()
	fTask.close()