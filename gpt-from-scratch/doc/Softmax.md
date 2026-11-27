## Softmax

We use - max inorder to get nummerical stabilty (if we have large weights we would get infinity by exp) and we use exp-family to get a distribution >= 0.

$
softmax(x_i) = \frac{exp(x_i - max)}{\sum exp(x_j - max)}
$