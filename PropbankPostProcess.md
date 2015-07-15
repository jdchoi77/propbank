## 1. How to run post-processing scripts ##

All post-processing scripts are under `/home/verbs/shared/cleardata/scripts`.

### 1.1. Generate byte indices of tree ###

To generate byte indices of all trees used for Propbank instances, type the following command.

```
./generate-byte-index.py <byte file> <tree directory>
```

`<tree directory>` is the path of top-level directory that contains all treebank files as in its sub-directories.  `<byte file>` is the output that contains all byte indices of the trees.  For example, if you want to generate a file containing byte indices of all trees in WSJ, type the following command.

```
./generate-byte-index.py byte.txt /home/verbs/shared/cleardata/ontonotes/on/english/annotations/parse/nw/wsj
```

This will recursively read all trees under `/home/verbs/shared/cleardata/ontonotes/on/english/annotations/parse/nw/wsj` and generate `byte.txt` containing byte indices of all trees in WSJ.

### 1.2. Post-process Propbank instances ###

Once you created a byte file, you are ready to post-process.  Type the following command to run the post-process script.

```
./post-process.py <propbank file> <byte file> <post-processed file>
```

`<propbank file>` is an input that contains Propbank instances to be post-processed. `<byte file>` is the output from `generate-byte-index.py`.  `<post-processed file>` is the output that contains post-processed Propbank instances.  The following section describes what things are done during the post-processing.
<br><br>

<h2>2. Things done by post-processing</h2>

<h3>2.1. Label changes</h3>

<ul><li>ARGM-RCL --> LINK-SLC<br>
</li><li>ARGM-SLC --> LINK-SLC<br>
</li><li>ARGM-PCR --> LINK-PCR</li></ul>

<h3>2.2. Traces</h3>

<ul><li>A trace is recognized as a node whose part-of-speech tag is <code>-NONE-</code>.<br>
</li><li>A trace must be annotated on a leaf node.  If a trace is annotated on a phrase node, post-process it so it is annotated on the leaf node (except for <code>*ICH*</code>, see Section 2.3).  For example, if a trace <code>*-1</code> from <code>(NP (-NONE- *-1))</code> is to be annotated, the annotation should be on <code>-NONE-</code>, not on <code>NP</code>.<br>
</li><li>If a trace is marked with an index (trace ID), link the trace with every other phrase marked with the same index in the tree.  For example, if <code>*T*-1 (8:0)</code> is annotated as <code>ARG0</code> of <code>was (9:0)</code>, then it should be linked with <code>WHNP-1 (7:1)</code> so that the result is <code>7:1*8:0</code>.<br>
<pre><code>((S<br>
  (NP-SBJ (PRP He))<br>
  (ADVP (RB certainly))<br>
  (VP (VBD did)<br>
      (RB not)<br>
      (VP (VB want)<br>
          (NP<br>
            (NP (DT a) (NN wife))<br>
            (SBAR<br>
              (WHNP-1 (WP who))<br>
              (S<br>
                (NP-SBJ (-NONE- *T*-1))<br>
                (VP (VBD was)<br>
                    (ADJP-PRD<br>
                      (ADJP (JJ fickle))<br>
                      (PP (IN as)<br>
                          (NP (NNP Ann))))))))))<br>
  (. .)))<br>
</code></pre></li></ul>

<h3>2.3. <code>*</code>ICH<code>*</code> traces</h3>

<ul><li>If a phrase node includes <code>*ICH*</code> trace as one of its descendants, take the trace ID of <code>*ICH*</code> and chain the phrase with every other phrase marked with the same trace ID.  For example, if <code>*-3 (4:0)</code> is annotated as <code>ARG1</code> of <code>given (3:0)</code>, it is first linked with <code>NP-SBJ-3 (0:1)</code> so the result is <code>0:1*4:0</code> (see Section 2.2).  Moreover, since <code>NP-SBJ-3</code> includes <code>*ICH*-1 (1:0)</code> as its descendant, it is again chained with <code>S-1 (5:2)</code> so the overall result is <code>0:1;5:2*4:0</code>.<br>
<pre><code>((S<br>
  (NP-SBJ-3 (NNS Orders)<br>
            (S (-NONE- *ICH*-1)))<br>
  (VP (VBD were)<br>
      (VP (VBN given)<br>
          (NP (-NONE- *-3))<br>
          (S-1<br>
            (NP-SBJ (-NONE- *PRO*))<br>
            (VP (TO to)<br>
                (VP (VB dig))))))<br>
  (. .)))<br>
</code></pre></li></ul>

<h3>2.4. LINK-PCR</h3>

<ul><li>When a numbered argument includes only a trace node and itS trace ID (if exists) points to <code>NP-SBJ</code>, link the trace node with <code>NP-SBJ</code> and annotate the result as <code>LINK-PCR</code>.  For the example in Section 2.3 where <code>*-3 (4:0)</code> is annotated as <code>ARG1</code>, the overall result <code>0:1;5:2*4:0</code> is labeled as <code>LINK-PCR</code> so that <code>4:0-ARG1</code> and <code>0:1;5:2*4:0-LINK-PCR</code>.</li></ul>

<ul><li>When a numbered argument include <code>*PRO*</code> trace, keep the <code>*PRO*</code> trace as the numbered argument and annotate all links (including <code>*PRO*</code>) as <code>LINK-PCR</code>.  For example, if <code>[automation machines] * [*PRO*-2] (5:1*9:0)</code> is annotated as <code>ARG0</code> of <code>work (11:0)</code>, first link <code>9:0</code> with <code>NP-2 (8:1)</code> (see Section 2.2), keep <code>9:0</code> as <code>ARG0</code> and annotate all links as <code>LINK-PCR</code>, so the result is <code>9:0-ARG0</code> and <code>5:1*8:1*9:0-LINK-PCR</code>.<br>
<pre><code>((S<br>
  (PP (IN In)<br>
      (NP (JJ other)<br>
          (NNS words)))<br>
  (, ,)<br>
  (PP-MNR<br>
    (IN like)<br>
    (NP<br>
      (NP (NN automation)<br>
          (NNS machines))<br>
      (VP (VBN designed)<br>
          (NP-2 (-NONE- *))<br>
          (S-CLR<br>
            (NP-SBJ (-NONE- *PRO*-2))<br>
            (VP (TO to)<br>
                (VP (VB work)<br>
                    (PP-MNR (IN in)<br>
                            (NP (NN tandem)))))))))<br>
...<br>
</code></pre></li></ul>

<h3>2.5. LINK-SLC</h3>

<ul><li>When a numbered argument includes <code>&amp;</code> operator, keep only the last trace as the numbered argument, link all nodes (including the trace) and annotate the result as <code>LINK-SLC</code>.  For example, if the annotation is <code>0:0&amp;1:0-ARG1</code> where <code>1:0</code> is a trace, it is post-processed to <code>1:0-ARG1</code> and <code>0:0*1:0-LINK-SLC</code>.</li></ul>

<ul><li>(deprecated) When an argument consists of a trace whose (POS-tag word) is <code>(-NONE- *)</code> and it is followed by <code>rel</code> node and the parent of the trace and all linked nodes are <code>NP</code>, keep the trace with the argument label and annotate all links to <code>LINK-SLC</code>.  For example, if <code>[Calloused fingers] * [*] (0:1*4:0)</code> is annotated as <code>ARG1</code> of <code>caress (3:0)</code>, check if both <code>0:1</code> and <code>4:1</code> are <code>NP</code> and <code>4:0</code> is followed by the <code>rel (3:0)</code>.  If it is, keep <code>4:0</code> as <code>ARG1</code> and annotate <code>0:1*4:0</code> as <code>LINK-SLC</code>, so the result is <code>4:0-ARG1</code> and <code>0:1*4:0-LINK-SLC</code>.<br>
<pre><code><br>
((S<br>
  (NP-SBJ<br>
    (NP (JJ Calloused) (NNS fingers))<br>
    (, ,)<br>
    (VP (VBN caressed)<br>
        (NP (-NONE- *))<br>
        (PP<br>
          (ADVP (RB only))<br>
          (IN by)<br>
          (NP-LGS<br>
            (NP (DT the) (NN smoothness))<br>
            (PP (IN of)<br>
                (NP (VBN polished) (NNS rosaries))))))<br>
</code></pre></li></ul>

<h3>2.6. ARGM-DSP</h3>

<ul><li>If the argument label is <code>ARGM-DSP</code>, take the very top-level phrase node and concatenate all leaf nodes under the phrase except for the ones belong to <code>PRN</code> phrase.  Then, annotate the concatenation with the same label as the numbered argument, say <i>A</i>, that contains the trace linked in <code>ARGM-DSP</code>.  Finally, remove the argument <i>A</i> from the annotation.  For example, if we have <code>0:2*6:0-ARGM-DSP</code> and <code>5:1-ARG1</code>, take the top-level phrase <code>S (0:2)</code> and concatenate all leaf nodes under <code>0:2</code> except <code>PRN (6:4)</code> so the result is <code>0:0,1:0,8:0,9:0,10:0</code>.  Then, annotation the concatenation with the label from <code>SBAR (5:1)</code> so the result is <code>0:0,1:0,8:0,9:0,10:0-ARG1</code>.  Finally, remove <code>5:1-ARG1</code> from the annotation so <code>0:0,1:0,8:0,9:0,10:0-ARG1</code> is the only <code>ARG1</code> in the annotation.<br>
<pre><code>((S<br>
  (NP-SBJ (PRP$ His)<br>
          (NN heart))<br>
  (PRN<br>
    (, ,)<br>
    (S<br>
      (NP-SBJ (PRP he))<br>
      (VP<br>
        (VBD discovered)<br>
        (SBAR<br>
          (-NONE- 0)<br>
          (S (-NONE- *?*)))))<br>
    (, ,))<br>
  (VP (VBD was)<br>
      (VP (VBG pounding)))<br>
  (. .)))<br>
</code></pre>