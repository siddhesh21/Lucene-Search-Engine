# Lucene-Search-Engine
TCD - Information Retrieval &amp; Web Search module Assessment. 

Continuous Assessment

To build a SearchEngine using Lucene Analyzers.
Built by making use of Eclipse IDE - 2020-09 written in Java executed results in terminal and evaluate performance using AWS EC2 Instance with Putty.

Following are the steps need to be followed after successfull access to my EC2 Instance.
If not 
Run - git clone https://github.com/siddhesh21/Lucene-Search-Engine.git

Make sure to have following pre-installed
Latest Java version (at the moment):  
Run - yum install -y java-1.8.0-openjdk-devel.x86_64
Latest Maven (at the moment):
Run - yum install -y apache-maven

Run following commands in Root user mode.

|**Steps/Commands**| **Description**|
|----------|-------------|
|cd Lucene-Search-Engine/ | Go to the directory to get started|
|mvn package| This command builds the maven project and packages them into a JAR, it will create a jar named "LuceneSE-0.0.1-SNAPSHOT.jar" compiled java file to execute different Analyzers and scoring functions|
|Choose Analyzer|1. English Analyzer|
|Choose Scoring Model|1. BM25 |
|comment|It  will create results.txt file in path Lucene-Search-Engine/src/results.txt|
|tar -xvzf trec_eval_latest.tar.gz| Unzip trec_eval files|
|cd trec_eval-9.0.7/ | change directory|
|make|Use it to describe any task where some files must be updated automatically from others whenever the others change.| 
|ls -lrt trec*|To see descriptive list of created trec_eval file to evaluate|
| ./trec_eval ../cran/QRelsCorrectedforTRECeval ../src/results.txt|It will evaluate QRelsCorrectedforTRECeval with our queried results.txt file|
|./trec_eval -m map -m P.5,10,50 ../cran/QRelsCorrectedforTRECeval ../src/results.txt| This command will map the specifi measures|

**Following OUTPUT is of**
**English Analyzer and BM25Similarity Score**
|**NAME**|**Output**|**Description**|
|----|------|-----------|
|runid|STANDARD|Name of the run (is the name given on the last field of the results file)|
|num_q|225|Total number of evaluated queries|
|num_ret|200754|Total number of retrieved documents|
|num_rel|1837|Total number of relevant documents (according to the qrels file)|
|num_rel_ret|1757|Total number of relevant documents retrieved (in the results file)|
|map|0.4070|Mean average precision (map)|
|iprec_at_recall_0.00|0.8138|Interpolated Recall - Precision Averages at 0.00 recall|
|iprec_at_recall_0.10|0.7752|Interpolated Recall - Precision Averages at 0.10 recall|
|P_5|0.4284|Precision of the 5 first documents|
|P_1000|0.078|Precision of the 1000 first documents|

**Format of command** 
```
$ ./trec_eval [-q] [-m measure] qrel_file results_file
```
The trec_eval is a tool used to evaluate rankings, either documents or any other information that is sorted by relevance. The evaluation is based on two files: The first, known as "qrels" (query relevances - QRelsCorrectedforTRECeval) lists the relevance judgements for each query. The second contains the rankings of documents returned by your RI system (results.txt file).
**Viewing a specific measure**
```
$ .\trec_eval -m map -m P.5,10 qrels_file results_file
```
**Note** 
1. qrels file must be of "query-id  0  document-id  relevance" format.
2. results.txt file must be of "query-id  Q0  document-id  rank  score  STANDARD" format.

**Successfully created Lucene Search Engine by**
1. Indexing
2. Searching
3. Quering
4. Evaluation
5. Scoring
