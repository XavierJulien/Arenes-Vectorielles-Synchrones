<!DOCTYPE HTML PUBLIC ".//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD><TITLE>Identification</TITLE></HEAD>
<BODY>
<H1 ALIGN=CENTER>
Identification de l'utilisateur
</H1>
<TABLE BORDER=1 ALIGN=CENTER>
<%
String a = request.getRemoteAddr();
String h = request.getRemoteHost();
String u = request.getRemoteUser();

if (u == null)
   u = "Anonyme";
%>
<TR><TD>REMOTE ADRESSE<TD>
<%= a %>
<TR><TD>REMOTE HOST<TD>
<%= h %>
<TR><TD>REMOTE USER<TD>
<%= u %>
</TABLE></BODY></HTML>
