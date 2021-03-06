#!/usr/local/bin/python3.1
# -*- coding: UTF-8 -*- 
# filename   : insert_null.py
# author     : Jinho D. Choi
# last update: 10/6/2010
import sys
from ssf_api import *

# initializes trans verbs
f_trans   = open('map_trans.txt')
s_intrans = set()
s_ditrans = set()

for line in f_trans:
	l = line.split()
	if   l[2] == 'I': s_intrans.add(l[0])
	elif l[2] == 'D': s_ditrans.add(l[0])

f_trans.close()

DREL_K1  = 'k1'
DREL_K1S = 'k1s'
DREL_K2  = 'k2'
DREL_K4  = 'k4'
DREL_K4A = 'k4a'
DREL_K7  = 'k7'
DREL_POF = "pof"

# {'aa':'huaa', 'ii':'huii', 'e':'huey', 'ne':'vaale', 'ne':'vaalaa', 'ne':'vaalii'}
#REL_PRO_VERBS = {'ा':'हुआ', 'ी':'हुई', 'े':'हुए', 'ने':'वाले', 'ने':'वाला', 'ने':'वाली'}
#REL_PRO_DRELS = set(['nmod__k1inv', 'nmod__k2inv'])
REL_PRO_DRELS = {'nmod__k1inv', 'nmod__k2inv', 'nmod__pofinv'}
REL_PRO       = 'RELPRO'

BIG_PRO_SUFFIX = ['ना', 'नी', 'ने']		# naa, nil, ne
#BIG_PRO_SUFFIX = ['ना']					# naa
BIG_PRO_NOT    = 'वाला'					# vaalaa
BIG_PRO_KAR    = 'कर'					# kar
BIG_PRO_TE     = 'ते'					# te
BIG_PRO_HUEY   = 'हुए'					# huey
BIG_PRO        = 'PRO'

PRO_AUR        = 'और'
PRO            = 'pro'

GAP_PRO        = 'gap_pro'

class InsertNull:
	# Inserts rel-pro chunks to 'tree'.
	# tree : Tree
	def insertRelPro(self, tree):
		size = len(tree)
		i    = 0
		
		while i < size:
			chunk = tree[i]; i += 1
			if not self._isRelProVerb(chunk): continue
			headId = chunk.getName()
			if not headId: continue
			drel = chunk.getDrel()
			
			if (drel[0] == 'nmod__k1inv' or drel[0] == 'nmod__pofinv') and not tree.existChild(DREL_K1, headId):
				tree.insertFirstChild(headId, self.getNullChunk(REL_PRO, DREL_K1, headId))
				i += 1; size += 1
			elif drel[0] == 'nmod__k2inv' and not tree.existChild(DREL_K2, headId):
				tree.insertLastChild(headId, self.getNullChunk(REL_PRO, DREL_K2, headId))
				i += 1; size += 1

	# Returns true if 'chunk' is a rel-pro verb.
	# chunk : Chunk
	def _isRelProVerb(self, chunk):
		if not chunk.isVerb(): return False
		if len(chunk) < 3    : return False
		drel = chunk.getDrel()
		if not drel or drel[0] not in REL_PRO_DRELS: return False
		
		return True
		'''
		for suffix in REL_PRO_VERBS:
			if chunk[1].wordEnds(suffix) and chunk[2].wordEquals(REL_PRO_VERBS[suffix]):
				return True
	
		return False
		'''

	# Inserts big-pro chunks to 'tree'.
	# tree : Tree
	def insertBigPro(self, tree):
		size = len(tree)
		i    = 0
		
		while i < size:
			chunk = tree[i]; i += 1
			if not self._isBigProVerb(tree, chunk): continue
			headId = chunk.getName()
			if not headId: continue
			
			if not tree.existChild(DREL_K1, headId):
				tree.insertFirstChild(headId, self.getNullChunk(BIG_PRO, DREL_K1, headId))
				i += 1; size += 1


	# Returns true if 'chunk' is a big-pro verb.
	# chunk : Chunk
	def _isBigProVerb(self, tree, chunk):
		if not chunk.isVerb()              : return False
		if chunk[1].wordEquals(BIG_PRO_KAR): return True
		if len(chunk) < 3                  : return False
		if chunk[2].wordEquals(BIG_PRO_KAR): return True
		
		for suffix in BIG_PRO_SUFFIX:
			if chunk[1].wordEnds(suffix) and not chunk[2].wordEquals(BIG_PRO_NOT):
				return True
				
		headId = chunk.getName()
		if not headId: return False
		if chunk[1].wordEnds(BIG_PRO_TE) and chunk[2].wordEquals(BIG_PRO_HUEY) and not tree.existChild(REL_PRO, headId):
			return True

		return False

	# Inserts pro chunks to 'tree'.
	# tree : Tree
	def insertPro(self, tree):
		size = len(tree)
		i    = 0
		
		while i < size:
			chunk = tree[i]; i += 1
			if not chunk.isFiniteVerb(): continue
			headId = chunk.getName()
			if not headId: continue
			if tree.existChild(DREL_POF, headId): continue
			mainV  = chunk.getMainVerb()
			if not mainV : continue
			lemma  = mainV.getLemma()
			
			if not tree.existChild(DREL_K1, headId) and not tree.existChild(DREL_K4A, headId):
				tree.insertFirstChild(headId, self.getNullChunk(PRO, DREL_K1, headId))
				i += 1; size += 1
			
			if not tree.existChild(DREL_K4, headId) and lemma in s_ditrans:
				tree.insertLastChild(headId, self.getNullChunk(PRO, DREL_K4, headId))
				i += 1; size += 1
			
			if not tree.existChild(DREL_K2, headId) and not tree.existChild(DREL_K1S, headId) and not tree.existChild(DREL_K7, headId) and lemma not in s_intrans:
				tree.insertLastChild(headId, self.getNullChunk(PRO, DREL_K2, headId))
				i += 1; size += 1

	# Inserts pro chunks to 'tree'.
	# tree : Tree
	def insertGapPro(self, tree):
		size = len(tree)
		i    = 0
		
		while i < size:
			chunk = tree[i]; i += 1
			if not chunk.isFiniteVerb(): continue
			drel = chunk.getDrel()
			if not drel or len(drel) < 2: continue
			head = tree.getChunk(drel[1])
			if head and not self._isProConjunct(head): continue
			headId = chunk.getName()
			if not headId: continue
			if tree.existChild(DREL_POF, headId): continue
			mainV  = chunk.getMainVerb()
			if not mainV : continue
			lemma  = mainV.getLemma()
			
			if not tree.existChild(DREL_K1, headId):
				tree.insertFirstChild(headId, self.getNullChunk(GAP_PRO, DREL_K1, headId))
				i += 1; size += 1
			
			if not tree.existChild(DREL_K4, headId) and lemma in s_ditrans:
				tree.insertLastChild(headId, self.getNullChunk(GAP_PRO, DREL_K4, headId))
				i += 1; size += 1
			
			if not tree.existChild(DREL_K2, headId) and lemma not in s_intrans:
				tree.insertLastChild(headId, self.getNullChunk(GAP_PRO, DREL_K2, headId))
				i += 1; size += 1




	# Returns a null chunk.
	# label: label of the null chunk (e.g., RELPRO, BIGPRO) : string
	# drel: dependency relation (either 'k1' or 'k2') : string
	# headId: ID of the head chunk
	def getNullChunk(self, label, drel, headId):
		ls = list()
		ls.append('0\t((\tNULL__NP\t<fs pbmrel=\''+label+'-'+drel+DELIM_DREL+headId+'\' name=\'NULL__NP\'>')
	#	ls.append('0\t((\tNULL__NP\t<fs pbmrel=\''+label+DELIM_DREL+headId+'\' name=\'NULL__NP\'>')
		ls.append('0.1\tNULL\tNULL\t<fs name=\'NULL\'>')
		ls.append('\t))')

		return Chunk(ls)
	
	# Returns true if 'chunk' is a conjunct for pro chunk.
	# chunk : Chunk
	def _isProConjunct(self, chunk):
		return chunk.isConjunct() and chunk[1].wordEquals(PRO_AUR)


SSF_FILE = sys.argv[1]
OUT_FILE = sys.argv[2]

ssf = SSF(SSF_FILE)
ins = InsertNull()

for tree in ssf.getTrees():
	ins.insertRelPro(tree)
	ins.insertBigPro(tree)
#	ins.insertGapPro(tree)
	ins.insertPro(tree)

ssf.print(OUT_FILE)




"""
ls = list()
ls.append('14\t((\tVGF\t<fs name=\'VGF2\' drel=\'ccof:CCP\' stype=\'declarative\' voice-type=\'active\'>')
ls.append('14.1\tलगा\tVM\t<fs af=\'लग,v,m,sg,any,,या,yA\' posn=\'260\' name=\'लगा\'>')
ls.append('14.2\tहै\tVAUX\t<fs af=\'है,v,any,sg,3,,है,hE\' posn=\'270\' name=\'है\'>')
ls.append('\t))')
chunk = Chunk(ls)
print(chunk.toStr(14))
"""
