--- Revision Note ---
7/21/2011 Removed unnecessary debug statements


-----User Manual-----


This extractor extracts algorithms (if exist) in a PDF file, along with the corresponding information of each detected algorithm. It takes two inputs which are the PDF file and paper id, and outputs an xml file containing the extracted algorithms and their corresponding information.

=============================
Run:

1. Compile the TextExtractor (java). Note that if Extractor.class is already in the directory, you may skip this step.

	javac -cp .:./libs/pdfbox-app-1.5.0.jar TextExtractor.java



2. Extract algorithms from a PDF file

2.1 Command Line Interface
	
	perl main.pl <pdf_file_path> <docID> <output_xml_file_path>

Where
	<pdf_file_path> : Path to the pdf file
	<docID>			: Document ID
	<output_xml_file_path> : Path to the output XML filename

Example:
	
	perl main.pl ./samples/10.1.1.111.8323.pdf 10.1.1.111.8323 ./samples/10.1.1.111.8323.xml
	

2.2 Function Interface 
	- Include main.pl
	- Call to extractAlgorithms($pdfFilePath, $docID, $outputXMLFilePath) in main.pl
	* May have to modify main.pl so that it is include-able by the caller perl program

==============================
A couple things:

- To change PDF-to-Text Extractor:

	Modify function "extractTextFromPDF" in main.pl.

	The current implementation has the perl code invoke the shell command to run a java program that extract the text from a pdf file. Make change to that part. Let me know if you have any question. 
	
- AlgoXmlGenerator.pl uses module Digest::SHA1. You might want to install the module in the machine before running the code (and don't forget to modify the header of AlgoXmlGenerator.pl).


==============================
XML output format:

-----------------------------------
<doc>
	<algorithm id="xxx01">
		<caption></caption>
		<reftext></reftext>
		<synopsis></synopsis>
		<paperid></paperid>
		<pagenum></pagenum>
		<checksum></checksum>
	</algorithm>

	<algorithm id="xxx02">
		<caption></caption>
		<reftext></reftext>
		<synopsis></synopsis>
		<paperid></paperid>
		<pagenum></pagenum>
		<checksum></checksum>
	</algorithm>

</doc>
-----------------------------------
id: algorithm id
caption: caption associated with the algorithm
reftext: a set of reference sentences related to the algorithm found in the paper
synopsis: synopsis of the algorithm
paperid: id of the paper where the algorith is detected
pagenum: page number in the paper where the algorithm is located
checksum: SHA1 checksum of the input PDF file


==============================
Sample XML output:

<doc>
	<algorithm id="10.1.1.111.8323_Algo_1">
		<caption>Algorithm 1 Residual resampling 1: Let ω̃ jt = ω</caption>
		<reftext>However, they offer flexibility and, in the case of residual resampling, may be computationally beneficial. 5.1 Residual resampling Residual sampling is a mostly-deterministic approach that enforces the number of copies of a particle retained during resampling to be (approximately) propor- tional to the weight of the sample. (Note that this is the expected result of random resampling.) The technique is shown in Algorithm 1. j t/∑ N i=1 ω i t 2: Retain k j = bNω̃ j tc copies of φ j t 3: Let Nr = N − k1 − . . .− kN 4: Obtain Nr i.i.d. draws (with replacement) from {φ1t , . . . , φ N t } w.p. proportional to Nω̃ jt − k j 5: ∀j, ω jt = 1/N Since the number of deterministically selected copies of all particles, ∑ N j=1 k j, may be less than N, random resampling is performed according to the residu- als Nω̃ jt − k j in Step 4 to prevent bias. </reftext>
		<synopsis>To our knowledge, all published RBPF SLAM algorithms employ the random resam- pling approach, which resamples particles with probability proportional to their importance weights. We briefly describe two alternative techniques from the sta- tistical literature (Liu, 2001), termed residual resampling and generalized resampling, and apply them to particle filtering SLAM. Our results indicate that the perfor- mance of these strategies in the context of SLAM is not appreciably better than random resampling. However, they offer flexibility and, in the case of residual resampling, may be computationally beneficial. 5.1 Residual resampling Residual sampling is a mostly-deterministic approach that enforces the number of copies of a particle retained during resampling to be (approximately) propor- tional to the weight of the sample. (Note that this is the expected result of random resampling.) The technique is shown in Algorithm 1. j t/∑ N i=1 ω i t 2: Retain k j = bNω̃ j tc copies of φ j t 3: Let Nr = N − k1 − . . .− kN 4: Obtain Nr i.i.d. draws (with replacement) from {φ1t , . . . , φ N t } w.p. proportional to Nω̃ jt − k j 5: ∀j, ω jt = 1/N Since the number of deterministically selected copies of all particles, ∑ N j=1 k j, may be less than N, random resampling is performed according to the residu- als Nω̃ jt − k j in Step 4 to prevent bias. According to Liu (2001), residual resampling can be shown to “dominate” ran- dom resampling in that it yields more accurate PDF approximations and is compa- rable or better in terms of computation. Perhaps the primary benefit is that residual resampling gives comparable performance to random resampling while consum- ing many fewer random numbers, which may be costly to generate, particularly in embedded scenarios. 5.2 Generalized resampling The idea of generalized resampling is to resample according to alternative prob- abilities {ait} instead of the usual importance weights {ω i t}. The intuition behind this approach, which is depicted in Algorithm 2, is that {ait} can be used to “mod- ify” the weights of particles, balancing focus (giving more presence to particles with high weights) with particle diversity. 2: Draw k from {1, . . . , N} according to ait, i = 1 . . .</synopsis>
		<paperid>10.1.1.111.8323</paperid>
		<pagenum>12</pagenum>
		<checksum>7af4c2fc1a7de21d156f9b3d520b0820f771dd56</checksum>
	</algorithm>

	<algorithm id="10.1.1.111.8323_Algo_2">
		<caption>Algorithm 2 Generalized resampling 1: for j = 1 . . . N do</caption>
		<reftext>The intuition behind this approach, which is depicted in Algorithm 2, is that {ait} can be used to “mod- ify” the weights of particles, balancing focus (giving more presence to particles with high weights) with particle diversity. 2: Draw k from {1, . . . , N} according to ait, i = 1 . . . </reftext>
		<synopsis>However, they offer flexibility and, in the case of residual resampling, may be computationally beneficial. 5.1 Residual resampling Residual sampling is a mostly-deterministic approach that enforces the number of copies of a particle retained during resampling to be (approximately) propor- tional to the weight of the sample. (Note that this is the expected result of random resampling.) The technique is shown in Algorithm 1. j t/∑ N i=1 ω i t 2: Retain k j = bNω̃ j tc copies of φ j t 3: Let Nr = N − k1 − . . .− kN 4: Obtain Nr i.i.d. draws (with replacement) from {φ1t , . . . , φ N t } w.p. proportional to Nω̃ jt − k j 5: ∀j, ω jt = 1/N Since the number of deterministically selected copies of all particles, ∑ N j=1 k j, may be less than N, random resampling is performed according to the residu- als Nω̃ jt − k j in Step 4 to prevent bias. According to Liu (2001), residual resampling can be shown to “dominate” ran- dom resampling in that it yields more accurate PDF approximations and is compa- rable or better in terms of computation. Perhaps the primary benefit is that residual resampling gives comparable performance to random resampling while consum- ing many fewer random numbers, which may be costly to generate, particularly in embedded scenarios. 5.2 Generalized resampling The idea of generalized resampling is to resample according to alternative prob- abilities {ait} instead of the usual importance weights {ω i t}. The intuition behind this approach, which is depicted in Algorithm 2, is that {ait} can be used to “mod- ify” the weights of particles, balancing focus (giving more presence to particles with high weights) with particle diversity. 2: Draw k from {1, . . . , N} according to ait, i = 1 . . . ... By picking α &lt; 1, the weight of seemingly poor particles is slightly amplified, giving them a “second chance.” (Note that the aits should be monotone in ωit since we generally want to discard bad samples and duplicate good ones.) The weights are reset nonuniformly after resampling to prevent bias. −40 −30 −20 −10 0 10 20 30 40−40 −30 −20 −10 0 10 20 30 40 (a) Sparse environment −40 −30 −20 −10 0 10 20 30 40−40 −30 −20 −10 0 10 20 30 40 lines represent the ground truth trajectories of the robot, which were kept the same for all simulations. The lighter gray lines depict several typical uncorrected odom- etry estimates of the robot’s trajectory. 6 Results Our experiments compared the standard FastSLAM 2 algorithm, the fixed-lag rough- ening (FLR) algorithm from Section 3, the block proposal (BP) distribution from Section 4, and FastSLAM 2 with residual (RES) and generalized (GEN) resampling as described in Section 5.</synopsis>
		<paperid>10.1.1.111.8323</paperid>
		<pagenum>12</pagenum>
		<checksum>7af4c2fc1a7de21d156f9b3d520b0820f771dd56</checksum>
	</algorithm>

	<algorithm id="10.1.1.111.8323_Fig_1">
		<caption>Figure 1: Simulated environments used to test RBPF SLAM algorithms. The envi- ronments consist of point landmarks placed uniformly at random. The solid dark</caption>
		<reftext>The environments, ground truth trajectories, and typical raw odometry estimates are shown in Figure 1. </reftext>
		<synopsis>The observation model used σr = 5 cm and σb = 0.3◦ with a sens- ing radius of 10 m, and the motion model used σx = 0.12d cos θ, σy = 0.12d sin θ and σθ = 0.12d + 0.24φ for translation d and rotation φ. Experiments were performed in a variety of simulated environments consisting of point features. We present results from two representative cases with randomly placed landmarks: a “sparse” map with a simple 27 sec. trajectory (no loops) and a “dense” map with a 63 sec. loop trajectory. The environments, ground truth trajectories, and typical raw odometry estimates are shown in Figure 1. All results were obtained by averaging 50 Monte Carlo trials of each simulation. 6.1 NEES comparison Webegin by comparing the normalized estimation error squared (NEES) (Bar-Shalom et al., 2001; Bailey et al., 2006) of the trajectory estimates produced by each algo- rithm. The NEES is a useful measure of filter consistency since it estimates the statistical distance of the filter estimate from the ground truth, i.e., it takes into account the filter’s estimate of its own error. For a ground truth pose xrt and an estimate x̂rt with covariance P̂ r t (computed from the weighted particles assuming they are approximately Gaussian), the NEES is: (xrt − x̂ r t)(P̂ r t) −1(xrt − x̂ r t) T (43) The recent paper by Bailey et al. (2006) gives more details about using NEES to measure RBPF SLAM consistency.</synopsis>
		<paperid>10.1.1.111.8323</paperid>
		<pagenum>13</pagenum>
		<checksum>7af4c2fc1a7de21d156f9b3d520b0820f771dd56</checksum>
	</algorithm>

</doc>

=============================
