#!/usr/bin/python
# -*- coding: UTF-8 -*- 
import sys
from ssf_api import *

DREL_K1 = 'k1'
DREL_K2 = 'k2'

# {'aa':'huaa', 'ii':'huii', 'e':'huey', 'ne':'vaale', 'ne':'vaalaa', 'ne':'vaalii'}
REL_PRO_VERBS = {'आ':'हुआ', 'ई':'हुई', 'ए':'हुए', 'ने':'वाले', 'ने':'वाला', 'ने':'वाली'}
REL_PRO_DRELS = {'nmod__k1inv', 'nmod__k2inv'}
REL_PRO       = 'RELPRO'

BIG_PRO_SUFFIX = ['ना', 'नी', 'ने']		# naa, nil, ne
BIG_PRO_NOT    = 'वाला'				# vaalaa


class InsertNull:
	# Inserts rel-pro chunks to 'tree'
	# tree : Tree
	def insertRelPro(self, tree):
		size = len(tree)
		i    = 0
		
		while i < size:
			chunk = tree[i]; i += 1
			if not self._isRelProVerb(chunk): continue
			headId = chunk.getName()
			
			if not chunk.isChild(DREL_K1, headId):
				tree.insertFirstChild(headId, self.getNullChunk(REL_PRO, DREL_K1, headId))
				i += 1; size += 1

			if not chunk.isChild(DREL_K2, headId):
				tree.insertLastChild(headId, self.getNullChunk(REL_PRO, DREL_K2, headId))
				i += 1; size += 1
				print(tree)

	# Returns true if 'chunk' is a rel-pro verb.
	# chunk : Chunk
	def _isRelProVerb(self, chunk):
		if not chunk.isVerb(): return False
		if len(chunk) < 3    : return False
		drel = chunk.getDrel()
		if not drel or drel[0] not in REL_PRO_DRELS: return False
		
		for suffix in REL_PRO_VERBS:
			if chunk[1].wordEnds(suffix) and chunk[2].wordEquals(REL_PRO_VERBS[suffix]):
				return True
	
		return False

	# Returns a null chunk.
	# label: label of the null chunk (e.g., RELPRO, BIGPRO) : string
	# drel: dependency relation (either 'k1' or 'k2') : string
	# headId: ID of the head chunk
	def getNullChunk(self, label, drel, headId):
		ls = list()
		ls.append('0\t((\tNULL__NP\t<fs pbmrel=\''+label+'-'+drel+DELIM_DREL+headId+'\' name=\'NULL__NP\'>')
		ls.append('0.1\tNULL\tNULL\t<fs name=\'NULL\'>')
		ls.append('\t))')

		return Chunk(ls)

SSF_FILE = sys.argv[1]
OUT_FILE = sys.argv[2]

ssf = SSF(SSF_FILE)
ins = InsertNull()

for tree in ssf.getTrees():
	ins.insertRelPro(tree)


#ssf.print(OUT_FILE)




"""
ls = list()
ls.append('14\t((\tVGF\t<fs name=\'VGF2\' drel=\'ccof:CCP\' stype=\'declarative\' voice-type=\'active\'>')
ls.append('14.1\tलगा\tVM\t<fs af=\'लग,v,m,sg,any,,या,yA\' posn=\'260\' name=\'लगा\'>')
ls.append('14.2\tहै\tVAUX\t<fs af=\'है,v,any,sg,3,,है,hE\' posn=\'270\' name=\'है\'>')
ls.append('\t))')
chunk = Chunk(ls)
print(chunk.toStr(14))
"""
