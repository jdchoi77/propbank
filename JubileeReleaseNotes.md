### Jubilee 2.20 (7/5/2011) ###
  * Bugfix: Jubilee now saves gold adjudications with gold IDs correctly.

### Jubilee 2.19 (5/20/2011) ###
  * Jubilee can now view contextual information.  On the menu, if you choose [- View Contexts](Treebank.md) or use a shortkey of "SHIFT+CTRL+o", it prompts a pop-up window showing the context of the chosen Treebank file.  The sentence for the currently selected tree is highlighted with yellow.

**The PropBank reader API is updated so it can read Treebank data faster.  If a task file is sorted by (treePath, treeId, predId), and there is a fair amount of instances read from the same Treebank file, it reads the instances noticeably faster now.**

### Jubilee 2.18a (2/2/2011) ###
  * Menu-shortkeys for Hindi empty categories are added.

### Jubilee 2.18 (11/5/2010) ###

  * TOP-node is now always displayed.
  * Hindi dependency tree is polished.

### Jubilee 2.17 (10/27/2010) ###

  * The skip mode now checks if both annotations and rolesets agree.
  * Hindi dependency tree module is added.

### Jubilee 2.16 (8/17/2010) ###

  * 'YY' tag is added for frameset ID representing 'No argument predicate.
  * For English, 'name' in example window becomes the title of the example.

### Jubilee 2.15 (7/21/2010) ###

  * In annotation mode, 'gold' annotations are no longer counted as parts of the double annotations.

### Jubilee 2.14 (6/17/2010) ###

  * Frameset filenames have been changed (choose.xml -> choose-v.xml).  In Jubilee task files, these frameset filenames are now inserted as an additional column.  Jubilee now supports the new format (but not the old format).

### Jubilee 2.13 (5/6/2010) ###

  * 'NN' and 'IE' tags are added for roleset IDs.

### Jubilee 2.12 (4/13/2010) ###

  * On the menu, there is a new item called 'View roleset comments' (shortcut: Ctrl+c).  When you select the menu, it will view the comments (notes) of the currently selected roleset (equivalent to frameset in Arabic), indicated in the corresponding frameset file.


### Jubilee 2.11 (1/4/2010) ###

  * If you choose an option to skip agreed annotations in gold-mode, it now changes user-IDs of all agreed annotations to 'gold' (before, it kept the the user-id of the first annotation).

  * You can specify the system folder by giving a new option (before you had to use the default system folder './system').  This can be useful if you want to keep system files (for different languages/projects) separately (e.g., system-english, system-arabic).  See #3 for more details about how to give options.

  * The command to launch Jubilee has been changed.  Now you can specify options more flexibly.

> `java -jar jubilee.jar -u <userId> [-m <max-annotations=2> -p <skip=0> -s <system-folder=system/>]`


### Jubilee 2.1 (7/24/2009) ###

  * Jubilee can now view parsed tree in text format.  Click 'Ctrl + T', then it will prompt a window showing the parsed-tree.  To close the window, type 'Ctrl + Shift + Q'.


### Jubilee 2.0 (2/24/2009) ###

  * When you launch Jubilee as gold, it will show three annotations.  'gold' annotation is a duplicate of one of the double-annotations, and 'annotator1' and 'annotator2' show two annotations done for the task.

  * There are three ways to launch Jubilee as gold now.

> `Usage: java -jar jubilee.jar gold <skip=0> <max-ann=2>`

> `<skip>` parameter enables you to skip double-annotations that are the same.  The default value is `0`, which implies not to skip.  So if you want to go through the entire annotations, type

> `java -jar jubilee.jar gold` or
> `java -jar jubilee.jar gold 0`

> If you want to skip the double-annotations that are the same, type

> `java -jar jubilee.jar gold 1`

> `<max-ann>` parameter enables you to choose the number of double-annotations.  The default value is `2`, which allows two annotations per task.  If you want to have three annotations per task, type (without skipping feature)

> `java -jar jubilee.jar gold 0 3`


### Jubilee 1.4 (6/11/2008) ###

  * When you accidentally delete ‘rel’, now you can get it back by typing 'Shift + Ctrl + /' on the node.
  * An option is added to enable more than 2 people to annotate the same task.  For example,

> `java -jar jubilee.jar jdchoi`

> will enable 2 people to annotate the same task (as default) whereas

> `java -jar jubilee.jar jdchoi 5`

> will enable 5 people to annotate the same task.
  * ‘lemma.xx’ is chosen as a default Chinese roleset-ID (a blank field was chosen before).
  * The text-area to show raw-sentences is expanded from 2 to 4 lines.
  * Some of the duplicated shortkeys were fixed (M-PRD: P -> 7).


### Jubilee 1.3 (2/29/2008) ###

  * You can view arguments in strings by typing ‘Ctrl + W’.  An pop window will prompt and show the arguments in strings.


### Jubilee 1.2b (1/2/2008) ###

  * ‘New Tasks’ in the open-dialog shows the list of task files that have either not been claimed or claimed only once by any annotators (it used to show only tasks that have not been claimed).  This enables two people annotate the same task in parallel (which is the general case).
  * You can add particle(s) to the verb relation.  Choose a particle then type ‘Ctrl + Shift + /’.  Jubilee will find the previous relation automatically and add the particle to the relation. (guideline p6)
  * Jubilee now supports adjudication.  Simply run Jubilee with user ID ‘gold’ (i.e. java -jar jubilee.jar gold) then it will run in adjudication mode.  Read the guideline p7 for more details.


### Jubilee 1.1b (10/12/2007) ###

  * The ‘Annotator’ combo-box is changed to a text-field.  This way, you don’t need to worry about switching the previous ID to your ID every time you move on to another tree.
  * ‘NO\_ARG’ button (for removing an argument-tag from a selected node) is now labeled ‘ERASE’ in Argument View (it still functions the same).
  * ‘-UNDEF’ button is added in Argument View.  Its description can be found from the guideline (p5).
  * A node with ‘rel’ tag became read-only.  In other words, you cannot either remove ‘rel’ tag or modify to some other tags.
  * ‘Sentence’ text-field is added in Treebank View.  It shows the raw sentence of the current tree showing in Treebank View.


### Jubilee 1.0b (10/10/2007) ###

  * Jubilee 1.0b is ready to use now.  Thanks to Professor Martha Palmer and Nianwen Xue for helping me along with the project.