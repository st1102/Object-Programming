#!/bin/sh
cat >treetmp.tex <<EOF
\documentclass[a4paper]{article}
\def\pgfsysdriver{pgfsys-dvipdfmx.def}
\usepackage{tikz}
\usetikzlibrary{trees}
\thispagestyle{empty}

\begin{document}

\tikzstyle{level 1}=[sibling distance=3.8cm]
\tikzstyle{level 2}=[sibling distance=1.8cm]
\tikzstyle{level 3}=[sibling distance=1cm]
\tikzstyle{edge from parent}=[draw,-]
\tikzstyle{every node}=[distance=1cm,draw,circle,thick,minimum size=2em]
\begin{tikzpicture}
\node
EOF
cat "$1" >> treetmp.tex
cat >> treetmp.tex <<EOF
;
\end{tikzpicture} 

\end{document}
EOF
platex treetmp && dvipdfmx treetmp && open treetmp.pdf
