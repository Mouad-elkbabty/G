<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : dico.xsl
    Created on : 25 octobre 2022, 16:32
    Author     : grangeq
    Description:
        Purpose of transformation follows.
-->

<xs:stylesheet xmlns:xs="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:dico="http://myGame/tux"
                xmlns="http://www.w3.org/1999/xhtml">
    <xs:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    
    <!-- ici template pour trier les mots en ordre alphabétique -->
    <!-- 
        
        on récupère les mots depuis la racine
        
    -->
    <xs:template match="/">
        <html>
            <head>
                <title>dico</title>
            </head>
            <body>
                <h1> Notre dictionnaire trié par ordre alphabétique </h1>
                <h2>Mots : </h2>
                    <xs:apply-templates select="//dico:mot">
                        <xs:sort select="."/>
                    </xs:apply-templates>
                
                
            </body>
        </html>
        
        <!-- ici template pour récupérer un mot -->
    <!-- 
        
        on récupère un mot
        
    -->
    </xs:template>
    <xs:template match="dico:mot">
        <xs:value-of select="." /><br/>
    </xs:template>

</xs:stylesheet>
