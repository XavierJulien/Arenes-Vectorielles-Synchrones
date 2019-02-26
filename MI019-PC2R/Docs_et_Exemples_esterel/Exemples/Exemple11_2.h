void factoriel (int *x, int n) {
  int i;

  for (i = 1; i <= n; ++i) {
    *x *= i;
  }
}
