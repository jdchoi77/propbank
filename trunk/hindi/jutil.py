# filename   : jutil.py
# author     : Jinho D. Choi
# last update: 4/19/2010
import math

# Converts 'i' into 'n' binary bits
# i: integer
# n: number of bits: integer
def bits(i, n): 
	return tuple((0,1)[i>>j & 1] for j in range(n-1,-1,-1)) 

# Returns sublist of 'L' indicated by 't'.
# L: list
# t: binary tuple
def getSub(L, t):
	ls = list()
	for i in range(0, len(L)):
		if t[i] == 1: ls.append(L[i])
	
	return ls

# Returns the permutation of 'L'.
# L: list
def permutations(L):
	ls   = list()
	size = len(L)
	for i in range(1, int(math.pow(2, size))):
		ls.append(getSub(L, bits(i, size)))
	
	return ls
