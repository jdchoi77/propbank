# -*- coding: UTF-8 -*- 
# filename   : ssf_api.py
# author     : Jinho D. Choi
# last update: 10/6/2010
import re

DELIM_NODE      = '\t'
DELIM_FS        = ' '
DELIM_KEY       = '='
DELIM_DREL      = ':'
DELIM_AF        = ','

KEY_DREL        = 'drel'
KEY_DMREL       = 'dmrel'
KEY_PBMREL      = 'pbmrel'
KEY_AF          = 'af'
KEY_VOICETYPE   = 'voicetype'

REG_FS          = re.compile('([<>\s])')

POS_VERB        = 'VG'
POS_FINITE_VERB = 'VGF'
POS_MAIN_VERB   = 'VM'
POS_CONJUNCT    = 'CCP'

BEGIN_DOCUMENT  = '<document'
BEGIN_HEAD      = '<head'
BEGIN_BODY      = '<body'
BEGIN_TABLE     = '<tb'
BEGIN_TEXT      = '<text'
BEGIN_SENTENCE  = '<Sentence'
BEGIN_SSF_CHUNK = '0\t((\tSSF'
BEGIN_FS        = '<fs'

END_DOCUMENT    = '</document>'
END_HEAD        = '</head>'
END_BODY        = '</body>'
END_TABLE       = '</tb>'
END_TEXT        = '</text>'
END_SENTENCE    = '</Sentence>'
END_CHUNK       = '\t))'
END_FS          = '>'

ATTR_NAME       = 'name'

############################## Begin: class SSF ##############################

class SSF:
	# Initializes SSF from 'filename'.
	# filanem: name of the SSF file (e.g., *.prun) : string
	def __init__(self, filename):
		fin = open(filename)
	
		self.str_document = self._getText(fin, BEGIN_DOCUMENT)			# <document..>
		self.str_head     = self._getText(fin, BEGIN_HEAD, END_HEAD)	# <head..>..</head>
		self.str_body     = self._getText(fin, BEGIN_BODY)				# <body..>
		self.ls_table     = list()
		
		tbText = self._getText(fin, BEGIN_TABLE, END_TABLE)				# <tb..>..</tb>
		while tbText:
			table = Table(tbText)
			self.ls_table.append(table)
			tbText = self._getText(fin, BEGIN_TABLE, END_TABLE)
		
		fin.close()
		
	# Returns the string of 'start'*['end'*], read from 'fin'.
	# This method is called from __init__().
	def _getText(self, fin, start, end=None):
		for text in fin:
			if text.strip(): break

		if not text.startswith(start): return None
		if not end                   : return text.strip()
		
		for line in fin:
			text += line
			if line.startswith(end)  : return text.strip()
			
		return None

	# Prints SSF to 'filename'.
	# filanem: name of the output file : string
	def print(self, filename):
		fout = open(filename, 'w')
		
		fout.write(self.str_document + '\n')
		fout.write(self.str_head     + '\n')
		fout.write(self.str_body     + '\n')
		
		for table in self.ls_table:
			fout.write(str(table) + '\n')
		
		fout.write(END_BODY     + '\n')
		fout.write(END_DOCUMENT + '\n')
		
		fout.close()
	
	# Returns the list of trees.
	def getTrees(self):
		ls = list()
		for table in self.ls_table:
			ls.extend(table.getTrees())
	
		return ls

############################## End  : class SSF   ##############################
############################## Begin: class Table ##############################

class Table:
	# Initializes the table from 'tbText'.
	# tbText: '<tb..>..</tb>'
	def __init__(self, tbText):
		lsText = tbText.split('\n')
		
		self.str_prefix = '\n'.join(self._getList(lsText, BEGIN_TABLE, BEGIN_TEXT))	# <tb..>..<text..>
		self.ls_tree    = list()
		
		lsTree = self._getList(lsText, BEGIN_SENTENCE, END_SENTENCE)	# <Sentence..>..</Sentence>
		while lsTree:
			self.ls_tree.append(Tree(lsTree))
			lsTree = self._getList(lsText, BEGIN_SENTENCE, END_SENTENCE)
	
	# Returns the list containing ['start', ... , 'end'].
	# This method is called from __init__().
	def _getList(self, lsText, start, end):
		if not lsText[0]: del lsText[0]		# remove the blank line if exists
		if not lsText[0].startswith(start): return None
		ls = list()
		
		for n,text in enumerate(lsText):
			ls.append(text)
			if text.startswith(end):
				del lsText[:n+1]
				return ls
		
		return None

	# Returns the string representation of the tree.
	def __str__(self):
		ls = list()
		ls.append(self.str_prefix)
		
		for tree in self.ls_tree:
			ls.append(str(tree) + '\n')
		
		ls.append(END_TEXT)
		ls.append(END_TABLE)
		
		return '\n'.join(ls)
	
	# Returns the list of trees in the table.
	def getTrees(self):
		return self.ls_tree
	
############################## End  : class Table ##############################
############################## Begin: class Tree  ##############################

class Tree(list):
	# Initializes the tree.
	# lsTree: list of tree lines from SSF: list
	def __init__(self, lsTree):
		self.str_id = lsTree[0]			# <Sentence ..>
		del lsTree[0], lsTree[-1]		# remove <Sentence ..>, </Sentence>
		
		if lsTree[0].startswith(BEGIN_SSF_CHUNK):
			self.is_ssfChunk = True
			del lsTree[0], lsTree[-1]	# remove '0 (( SSF', '))'
		else:
			self.is_ssfChunk = False
		
		chunk = self._getChunk(lsTree)
		while chunk:
			self.append(chunk)
			chunk = self._getChunk(lsTree)
	
	# Returns the first chunk in 'lsTree' and removes the part from 'lsTree'. 
	# This method is called from __init__(lsTree).
	# lsTree: list of tree lines from SSF: list
	def _getChunk(self, lsTree):
		ls = list()
		
		for n,line in enumerate(lsTree):
			ls.append(line)
			if line.startswith(END_CHUNK):
				del lsTree[:n+1]
				return Chunk(ls)

		return None
		
	# Returns the string representation of the tree.
	def __str__(self):
		ls = list()
		ls.append(self.str_id)
		if self.is_ssfChunk: ls.append(BEGIN_SSF_CHUNK)
		
		for i,chunk in enumerate(self):
			ls.append(chunk.toStr(i+1))
		
		if self.is_ssfChunk: ls.append(END_CHUNK)
		ls.append(END_SENTENCE)
	
		return '\n'.join(ls)
		
	def getChunk(self, name):
		for chunk in self:
			if chunk.nameEquals(name):
				return chunk
		
		return None
	
	# Returns true if there exists a child of 'headId' with a dependency relation 'drel'.
	# drel: dependency relation (e.g., k1) : string
	# headId: ID of the head chunk (e.g., VGF) : string
	def existChild(self, drel, headId):
		for chunk in self:
			if chunk.isChild(drel, headId): return True
		
		return False

	# Returns the list of children of 'headId'.
	# headId: ID of the head chunk (e.g., VGF) : string
	def getChildren(self, headId):
		ls = list()
		
		for chunk in self:
			if chunk.isChild(None, headId):
				ls.append(chunk)
		
		return ls
	
	# Returns the list of children of 'headId' that are verbs.
	# headId: ID of the head chunk (e.g., VGF) : string
	def getVerbChildren(self, headId):
		ls = list()
		
		for chunk in self:
			if chunk.isChild(None, headId) and chunk.isVerb():
				ls.append(chunk)
		
		return ls
	
	# Inserts 'node' as the first child of 'headId'.
	# node : child of 'headId' : Node
	# headId: ID of the head chunk (e.g., VGF) : string
	def insertFirstChild(self, headId, node):
		for i,chunk in enumerate(self):
			if chunk.isChild(None, headId) or chunk.getName() == headId:
				self.insert(i, node)
				break
	
	# Inserts 'node' as the last child of 'headId'.
	# node : child of 'headId' : Node
	# headId: ID of the head chunk (e.g., VGF) : string
	def insertLastChild(self, headId, node):
		for i,chunk in enumerate(self):
			if chunk.getName() == headId:
				self.insert(i, node)
				break
	
	def toJubilee(self):
		lTree = ['((SSF']
		for chunk in self:
			lTree.append('    '+chunk.toJubilee())
		lTree.append('))')
		
		return '\n'.join(lTree)
	
	def generateTokenIDs(self):
		tokId = 0
		
		for chunk in self:
			for i,node in enumerate(chunk[1:]):
				node.tokenId = tokId + i
			
			tokId += len(chunk) - 1
			
	def isCycle(self, name):
		chunk = self.getChunk(name)
		rel   = chunk.getRel()
		
		while rel and len(rel) >= 2:
			chunk = self.getChunk(rel[1])
			if chunk.getName() == name:
				return True
			rel = chunk.getRel()
		
		return False
			
	
	
############################## End  : class Tree  ##############################
############################## Begin: class Chunk ##############################

class Chunk(list):
	# Initializes the chunk.
	# lsChunk: list of chunk lines from SSF : list
	def __init__(self, lsChunk):
		for line in lsChunk[:-1]:	# discard '))' at the end
			ls   = line.split(DELIM_NODE)
			word = ls[1]
			pos  = ls[2]
			if len(ls) > 3: fs = ls[3]
			else          : fs = None
			
			self.append(Node(word, pos, fs))
	
	# Returns the string representation of the chunk.
	# chunkId: id of the chunk : integer
	def toStr(self, chunkId):
		ls = list()
		ls.append(str(chunkId) + DELIM_NODE + str(self[0]))
		
		for i in range(1, len(self)):
			ls.append(str(chunkId)+'.'+str(i) + DELIM_NODE + str(self[i]))
		
		ls.append(END_CHUNK)
		return '\n'.join(ls)

	# Returns true if it is a verb-chunk.
	def isVerb(self):
		return self[0].posStarts(POS_VERB)
	
	def isFiniteVerb(self):
		return self[0].posEquals(POS_FINITE_VERB)
	
	# Returns true if it is a conjunct-chunk.
	def isConjunct(self):
		return self[0].posEquals(POS_CONJUNCT)
	
	# Returns ['dependency relation', 'headId'] list of the chunk.
	def getDrel(self):
		if not self[0].dic_fs: return None
		
		if KEY_DREL in self[0].dic_fs:
			return self[0].dic_fs[KEY_DREL]
		
		return None
	
	# Returns ['dm-dependency relation', 'headId'] list of the chunk.
	def getDMrel(self):
		if not self[0].dic_fs: return None
		
		if KEY_DMREL in self[0].dic_fs:
			return self[0].dic_fs[KEY_DMREL]
		
		return None
	
	# Returns ['pbm-dependency relation', 'headId'] list of the chunk.
	def getPBMrel(self):
		if not self[0].dic_fs: return None
		
		if KEY_PBMREL in self[0].dic_fs:
			return self[0].dic_fs[KEY_PBMREL]
		
		return None
	
	def getMrel(self):
		mrel = self.getDMrel()
		if mrel: return mrel
		else   : return self.getPBMrel()
	
	def getRel(self):
		rel = self.getDrel()
		if rel: return rel
		else  : return self.getMrel()
	
	# Returns true if the chunk is a child of 'headId' with a dependency relation 'drel'.
	# drel: dependency relation (e.g., k1) : string
	# headId: ID of the head chunk (e.g., VGF) : string
	def isChild(self, drel, headId):
		minfo = self.getMrel()
		if minfo:
			if len(minfo) < 2: return False
			if not drel: return minfo[1] == headId
			return minfo[0].startswith(drel) and minfo[1] == headId
		
		dinfo = self.getDrel()
		if not dinfo or len(dinfo) < 2: return False
		if not drel : return dinfo[1] == headId
		return dinfo[0].startswith(drel) and dinfo[1] == headId
	
	# Returns the name of the chunk.
	def getName(self):
		return self[0].getName()
	
	def getAnyRelInString(self):
		rel = self.getDrel()
		if rel: return rel
		rel = self.getDMrel()
		if rel: return rel
		rel = self.getPBMrel()
		if rel: return rel
		
		return ['root','ROOT']
	
	def toJubilee(self):
		if self.getPBMrel(): lsTop = ['NULL__NP']
		else               : lsTop = [self[0].getName()]	# ['NP3']
		lsTop.extend(self.getAnyRelInString())						# ['pof','VGNN']
		voiceType = self[0].getVoiceType()
		if voiceType: lsTop.append(voiceType)

	#	print(lsTop)		
		ls = [':'.join(lsTop)]
		for i in range(1, len(self)):
			ls.append(self[i].toJubilee())
		
		return '(' + ' '.join(ls) + ')'
	
	def getMainVerb(self):
		for node in self[1:]:
			if node.posEquals(POS_MAIN_VERB):
				return node
		
		return None
	
	def nameEquals(self, name):
		return self[0].nameEquals(name)
		

############################## End  : class Chunk ##############################
############################## Begin: class Node  ##############################

class Node:
	# Initializes the node.
	# word: Hindi word-form or '((' : string
	# pos : part-of-speech tag : string
	# fs  : '<fs ..>' : string
	def __init__(self, word, pos, fs):
		self.str_word = word
		self.str_pos  = pos
		self.dic_fs   = self._getFs(fs)
		self.tokenId  = -1
		
	# Returns the dictionary of 'fs'.
	# This method is called from __init__().
	# fs: '<fs ..>' : string
	def _getFs(self, fs):
		if not fs: return None
		lsFs = REG_FS.split(fs)
		dic  = dict()
		
		for item in lsFs:
			if DELIM_KEY not in item: continue	# skip non 'key=value'
			kv  = item.split(DELIM_KEY)
			key = kv[0]
			val = kv[1]
			if val[0] == '\'' and val[-1] == '\'': val = val[1:-1]	# strip single quotes

			if   key == KEY_DREL  : dic[key] = val.split(DELIM_DREL)
			elif key == KEY_DMREL : dic[key] = val.split(DELIM_DREL)
			elif key == KEY_PBMREL: dic[key] = val.split(DELIM_DREL)
			elif key == KEY_AF    : dic[key] = val.split(DELIM_AF)
			else                  : dic[key] = val
		
		return dic

	# Returns the string representation of the node.
	def __str__(self):
		ls = list()
		ls.append(self.str_word)
		ls.append(self.str_pos)
		if self.dic_fs: ls.append(self._strFs())

		return DELIM_NODE.join(ls)
	
	# Returns the string representation of <fs>.
	# This method is called from __str__().
	def _strFs(self):
		ls = list()
		ls.append(BEGIN_FS)
		
		if KEY_AF in self.dic_fs:
			ls.append(KEY_AF + DELIM_KEY + '\'' + DELIM_AF.join(self.dic_fs[KEY_AF]) + '\'')
		
		for key in self.dic_fs:
			if key == KEY_AF: continue
			val = self.dic_fs[key]
			if   key == KEY_DREL  : val = DELIM_DREL.join(val)
			elif key == KEY_DMREL : val = DELIM_DREL.join(val)
			elif key == KEY_PBMREL: val = DELIM_DREL.join(val)
			elif key == KEY_AF    : val = DELIM_AF  .join(val)
			ls.append(key + DELIM_KEY + '\'' + val + '\'')
			
		return DELIM_FS.join(ls) + END_FS

	# Returns true if the word ends with 'word'.
	# word: string
	def wordEnds(self, word):
		return self.str_word.endswith(word)

	# Returns true if the word equals to 'word'.
	# word: string
	def wordEquals(self, word):
		return self.str_word == word
	
	# Returns true if the pos-tag starts with 'pos'.
	# pos: string
	def posStarts(self, pos):
		return self.str_pos.startswith(pos)

	# Returns true if the pos-tag is equal to 'pos'.
	# pos: string
	def posEquals(self, pos):
		return self.str_pos == pos

	# Returns the name of the node.
	def getName(self):
		if not self.dic_fs: return None
		
		if ATTR_NAME in self.dic_fs:
			return self.dic_fs[ATTR_NAME]
		
		return None
	
	def getPos(self):
		return self.str_pos
	
	def nameEquals(self, name):
		curr = self.getName();
		if curr: return curr == name
		return False

	# Returns the feature-value of 'key'.
	# key: feature-key (e.g., 'drel') : string 
	def getFeatureValue(self, key):
		return self.dic_fs[key]
	
	# Adds (key, value) to the feature-set.
	# key  : feature-key (e.g., 'drel') : string
	# value: feature-value (e.g., 'k1') : string
	def addFs(self, key, value):
		self.dic_fs[key] = value
	
	def getAF(self):
		if not self.dic_fs: return None
		if not KEY_AF in self.dic_fs: return None
		return self.dic_fs[KEY_AF]
	
	def getVoiceType(self):
		if not self.dic_fs: return None
		if KEY_VOICETYPE in self.dic_fs:
			return self.dic_fs[KEY_VOICETYPE]
		else:
			return None
	
	def getWord(self):
		return self.str_word
	
	def getLemma(self):
		af = self.getAF()
		if af and af[0]: return af[0]
		return self.str_word
	
	def setLemma(self, lemma):
		af = self.getAF()
		if not af:
			af = self.getBlankAF()
		
		af[0] = lemma
	
	def getBlankAF(self):
		return ['','','','','','','','']
	
	# node representation for Jubilee
	def toJubilee(self):
		word = self.str_word.replace('(','[')
		word = word.replace(')',']')
		return '(' + self.str_pos + ' ' + word + ')'

############################## End  : class Node ##############################