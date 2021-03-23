
# MCD

spent:2h, date:20/02/2021, tasks: init mcd on jhipster 
spent:

## Transform Jhipster JDL to Real JAVA Class

https://start.jhipster.tech/jdl-studio/

* Install JDL Plugin to Eclipse
* C:\Marc\projects\optimiser\src\main\resources\com\chantier\jhipster-jdl.jdl
* Convert to Java: https://www.jhipster.tech/installation/ , https://www.jhipster.tech/creating-an-entity/
	> npm install -g generator-jhipster
	> jhipster jdl ./my-jdl-file.jdl --json-only -- jhipster jdl C:\Marc\projects\optimiser\src\main\resources\com\chantier\jhipster-jdl.jdl
		>> a besoin d'un node plus recent :( = l'updater / https://phoenixnap.com/kb/update-node-js-version
		>> npm cache clean -f
		>> npm install -g n   : n = node version manager
		>> n stable   : pour avoir la version stable de node.js
		>> a voir si npm est updaté egalement (j'étais en version 6.9 au lieu de 7.5.4)
		    >>> au pire je fais: npm install -g npm
		    C:\Marc\projects\optimiser\src\main\resources\com\chantier>npm install -g npm
npm ERR! path C:\Marc\softs\node-v10.16.1-win-x64\npm.cmd
			>>> redownload de node
				>>> set PATH="C:\Marc\softs\node-v15.9.0-win-x64";%PATH%


# Planning SOLVER

spent: 3h + 3h

* Understand examples
   - what are BendableScore ?
   - Pq dès fois j'ai des comparator et pas: on dirait c'est qu'à choix egale il faut privilégier une entity par rapport à une autre
   - ConstraintStreams ? https://www.optaplanner.org/blog/2020/04/07/ConstraintStreams.html
   
* check best practices
	- transformer des many to many en 2 one to many

* implements to our use case
