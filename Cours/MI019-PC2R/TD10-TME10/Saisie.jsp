<!DOCTYPE HTML PUBLIC ".//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD><TITLE>Identification</TITLE></HEAD>
<BODY>
<H1 ALIGN=CENTER>
Formulaire de saisie dat&eacute; du
<%= (new java.util.Date(System.currentTimeMillis())).toString() %>

</H1>
<form method=GET
      action="http://10.0.0.1:8080/examples/servlet/Saisie">
  <TABLE BORDER=0>
    <TR VALIGN=TOP>
      <TD ALIGN=RIGHT>Nom :</TD>
      <TD ALIGN=LEFT>
         <INPUT TYPE=TEXT NAME=NOM SIZE=32 MAXLENGTH=80> </TD>
    <TR VALIGN=TOP>
      <TD ALIGN=RIGHT>Pr&eacute;nom :</TD>
      <TD ALIGN=LEFT>
         <INPUT TYPE=TEXT NAME=PRENOM SIZE=32 MAXLENGTH=80> </TD>
    <TR VALIGN=TOP>
      <TD COLSPAN=2 ALIGN=CENTER>
         <INPUT TYPE=SUBMIT VALUE="Envoyer"> </TD>
  </TABLE></FORM></BODY></HTML>

