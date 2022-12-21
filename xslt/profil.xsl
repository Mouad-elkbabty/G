<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : profil.xsl
    Created on : 29 octobre 2022, 14:41
    Author     : Quentin
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:profil="http://myGame/tux"
                xmlns="http://www.w3.org/1999/xhtml">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    
    
    <!-- Cette template permet de créer une page de profil d'un joueur -->
    <!-- Détails : 
        
        un profil a un nom, une photo de profil, les parties jouées du joueur
        et enfin les scores du joueur.
            
    -->
    <xsl:template match="/">
        <html>
            <head>
                <title>Profil</title>
                <link href="./profil.css" rel="stylesheet" type="text/css"/>
            </head>
            <body>
                <div>
                <table id="tableId">
                    <tr>
                        <td>
                            <img src="./images/Kratos.jpg" alt="Kratos de God of War"/><br/><br/>
                        </td>
                        <td id="nomP">
                            <b><xsl:apply-templates select="//profil:nom"/></b><br/><br/>
                        </td>
                    </tr>
                </table>

                <b id="pj"> Parties jouées : </b><br/><br/>
                <table id="tableStats">
                    <tr> 
                        <th> 
                            Date : 
                        </th>
                        <th> 
                            Temps :
                        </th>
                        <th> 
                            Niveau :
                        </th>
                        <th> 
                            Mot :
                        </th>
                        <th> 
                            Score :
                        </th>
                    </tr>
                    <xsl:apply-templates select="//profil:partie"/>
                </table>
                </div>
            </body>
        </html>
    </xsl:template>


    <!-- Cette template permet de récuperer le nom -->
    <!--  
        on récupère simplement l'élément "nom" de profil
    -->
    <xsl:template match="profil:nom">
        <xsl:value-of select="."/>
    </xsl:template>
    
    
    
  <!-- Cette template affiche les infos pour chaque partie dans un tableau -->
  <!-- 
        on affiche toutes les infos sauf trouvé car comme trouvé peut etre absent 
        ça ne fait pas très esthétique de laisser une case vide.
    -->
    <xsl:template match="profil:partie">
        <tr>
            <td>
                <xsl:value-of select="@date"/>
            </td>
            <td>
                <xsl:value-of select="profil:temps"/>
            </td>
            <td>
                <xsl:value-of select="profil:niveau/@niv"/>
            </td>
            <td>
                <xsl:value-of select="profil:niveau/profil:mot"/>
            </td>
            <td>
                <xsl:value-of select="(number(./profil:niveau/@niv) * 100)  div number(./profil:temps)"/>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
