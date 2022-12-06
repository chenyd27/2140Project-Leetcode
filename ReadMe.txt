# version of environment : elasticsearch-7.17.6, vue.cli 5.0.8

Steps to run:

# Step 1: Run the elasticsearch engine.

# Step 2: Run the LeetcodeSearchApplication.java to start the program.

# Step 3: Because you might have nothing in your elasticsearch collection, so you may need to run the Web crawler first to get some data.
         For getting the leetcode solutions information, you can run the localhost:9000/parseleetcode to crawl some solutions from the website.
         You can modify the com.leetcode.utils.HtmlParseUtil.java file to change the list of solutions you want, you can change the first loop in the getQuestion() method.

# Step 4: After crawling data, you can run the vue cli to start the front end program. Command is : vue ui.

# Step 5: Run the localhost:8080/#/question to start the program.