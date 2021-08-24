# primer-evaluator-webapp
Final assignment of Java 1 and Webbased systems 1

This is the front end with an incorporated back end of the final assignment of Java 1 and Webbased 1 of imgorter.

This is the primer evaluator.
The primer evaluator accepts 1 or 2 primers in the form of a manual upload or file upload.
The evaluator then calculates and checks the following properties of each primer:
- The nucleotides it contains (must be only ACTG)
- The validity of the sequence(must have a forward primer in the form of 5'-ACTG-3' and a reverse primer in the form of 3'-ACTG-5')
- The length of the sequence (must be between 18-22 bases long)
- The GC%: the percentage of G and C nucleotides (must be between 50-55%)
- The melting temperature: the temperature the primer melts (must be between 55 and 65 degrees Celsius)
- Homopolymer length: the consecutive number of a nucleotide in a row, like AAAA (must not be larger than 4)
- Intramolecular identity: the number of nucleotides that adhere to the reverse primer where the reverse primer is it's own kind (up to the user to determine the range, depends on usage of the primer)
- In case of two primers: the number of nucleotides that adhere to the reverse primer Intermolecular identity (up to the user to determine the range, depends on usage of the primer).

To run this yourself:
1. Download this repository
2. Open the repository in an IDE of your choice
3. Add TomCat as your localhost server
4. Add the four following environmental variables: DB_DATABASE, DB_HOST, DB_USERNAME and DB_PASSWORD
5. Change the file locations in FileUploadServlet.java on line 24 and 31 to existing locations on your pc
6. Run with TomCat as your localhost server
