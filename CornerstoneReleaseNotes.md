### Cornerstone 1.37 (8/8/2012) ###
  * VerbNet annotation fields are added for Arabic.

### Cornerstone 1.36 (5/5/2012) ###
  * Source field is added for English.

### Cornerstone 1.35 (6/17/2010) ###
  * Arabic frameset IDs naming has been changed (f1 -> 01).

### Cornerstone 1.34 (5/4/2010) ###
  * Bugfixes when reading files stored in Microsoft Windows platform.

### Cornerstone 1.33 (4/12/2010) ###
  * I added the 'vtype' field under Roleset.  The field gives an option of choosing either 'unaccusative' or 'unergative', but the values can be updated.  Furthermore, I made 'vncls' and framnet' fields invisible for Hindi (they are currently not used).  When you decide to add VerbNet or FrameNet info, it is very easy to make them visible again.
  * I added the 'drel' field for each role.  The field contains dependency relations that Archna sent out earlier.  Again, I made 'Add Vnrole' button invisible for now but we can always get that back.

### Cornerstone 1.32 (3/29/2010) ###
  * Cornerstone now can read 'lemma.xml', 'lemma-v.xml', and 'lemma-n.xml'.
  * For Arabic, Cornerstone enforces the verb ID same as the filename.  For example, if the filename is any of ones in #1, the ID field takes 'lemma' as the value.


### Cornerstone 1.31 (3/15/2010) ###
  * Hindi module is added.
  * Bugfix: in English/Hindi example frames, the value for 'name' field used to get copied to the values for 'type' and 'src' fields.  It is fixed now.


### Cornerstone 1.3 (9/25/2009) ###
  * Cornerstone is now out of a beta version.
  * Cornerstone automatically saves the annotation for the previous frameset file as you open or create a new frameset file.  Note that you still need to save the annotation for the last frameset file you work on.


### Cornerstone 1.3b (8/13/2009) ###

  * For non-english mode, Verb IDs were not saved properly.  This is fixed in version 1.3b.
  * When you create a frameset file, Cornerstone automatically detects if the filename contains '.xml' as an extension and if it does not, it appends the extension at the end.
  * For english-mode, when you change the lemma of the first predicate, Cornerstone automatically changes all roleset IDs with respect to the lemma.  For example, 'take.xml' has 'take' as the first lemma and 'take\_away' as the second lemma and many rolesets such as 'take.01', 'take.02', etc.  If you change the first lemma 'take' to 'untake', it will automatically change all roleset IDs from 'take.##' to 'untake.##'.  The roleset IDs do not get changed when you change any other lemma (e.g. 'take\_away' to 'untake\_away').  This is useful when you try to create a new frameset file on top of the existing one.
  * Followed by #3, I restored 'edit Roleset ID' option back since such option is necessary to make changes from the existing frameset files.


### Cornerstone 1.2b (7/7/2009) ###

  * When you add a predicate in English mode, it now automatically converts any space in lemma to '`_`'.  For example, if you add a predicate `take in`, Cornerstone will convert the lemma to `take_in`.
  * Cornerstone now automatically assigns the frameset ID (roleset ID in English), so you don't need to keep track of what number you last entered.
  * Cornerstone now remembers the last directory you either open or save a file.


### Cornerstone 1.1b (6/21/2009) ###

  * Cornerstone now supports 3 languages, Arabic, English, and Chinese.  Hindi is coming on the way.


### Cornerstone 1.0b (6/18/2009) ###

  * Cornerstone is read to use now.  Thanks to Professor Martha Palmer for helping me along with the project.