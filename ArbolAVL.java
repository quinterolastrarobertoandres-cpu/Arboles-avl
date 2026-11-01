
package arbolavl;

public class ArbolAVL {

    // Clase Nodo
    static class Nodo {
        int dato;
        int altura;
        Nodo izquierdo;
        Nodo derecho;

        Nodo(int dato) {
            this.dato = dato;
            this.altura = 1;
        }
    }

    private Nodo raiz;

    // Obtener altura de un nodo
    private int altura(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }
        return nodo.altura;
    }

    // Calcular factor de equilibrio
    private int factorBalance(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }
        return altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    // Actualizar altura
    private void actualizarAltura(Nodo nodo) {
        nodo.altura = 1 + Math.max(
                altura(nodo.izquierdo),
                altura(nodo.derecho)
        );
    }

    // Rotacion simple a la derecha
    private Nodo rotacionDerecha(Nodo y) {
        Nodo x = y.izquierdo;
        Nodo temporal = x.derecho;

        x.derecho = y;
        y.izquierdo = temporal;

        actualizarAltura(y);
        actualizarAltura(x);

        return x;
    }

    // Rotacion simple a la izquierda
    private Nodo rotacionIzquierda(Nodo x) {
        Nodo y = x.derecho;
        Nodo temporal = y.izquierdo;

        y.izquierdo = x;
        x.derecho = temporal;

        actualizarAltura(x);
        actualizarAltura(y);

        return y;
    }

    // =========================
    // INSERCION
    // =========================

    public void insertar(int dato) {
        raiz = insertar(raiz, dato);
    }

    private Nodo insertar(Nodo nodo, int dato) {

        if (nodo == null) {
            return new Nodo(dato);
        }

        if (dato < nodo.dato) {
            nodo.izquierdo = insertar(nodo.izquierdo, dato);
        } else if (dato > nodo.dato) {
            nodo.derecho = insertar(nodo.derecho, dato);
        } else {
            return nodo;
        }

        actualizarAltura(nodo);

        int balance = factorBalance(nodo);

        // Caso LL: rotacion simple a la derecha
        if (balance > 1 && dato < nodo.izquierdo.dato) {
            return rotacionDerecha(nodo);
        }

        // Caso RR: rotacion simple a la izquierda
        if (balance < -1 && dato > nodo.derecho.dato) {
            return rotacionIzquierda(nodo);
        }

        // Caso LR: rotacion doble izquierda-derecha
        if (balance > 1 && dato > nodo.izquierdo.dato) {
            nodo.izquierdo =
                    rotacionIzquierda(nodo.izquierdo);
            return rotacionDerecha(nodo);
        }

        // Caso RL: rotacion doble derecha-izquierda
        if (balance < -1 && dato < nodo.derecho.dato) {
            nodo.derecho =
                    rotacionDerecha(nodo.derecho);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    // =========================
    // ELIMINACION
    // =========================

    public void eliminar(int dato) {
        raiz = eliminar(raiz, dato);
    }

    private Nodo eliminar(Nodo nodo, int dato) {

        if (nodo == null) {
            return null;
        }

        if (dato < nodo.dato) {
            nodo.izquierdo = eliminar(nodo.izquierdo, dato);

        } else if (dato > nodo.dato) {
            nodo.derecho = eliminar(nodo.derecho, dato);

        } else {

            // Caso 1: nodo sin hijos
            if (nodo.izquierdo == null
                    && nodo.derecho == null) {
                return null;
            }

            // Caso 2: nodo con un solo hijo
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            }

            if (nodo.derecho == null) {
                return nodo.izquierdo;
            }

            // Caso 3: nodo con dos hijos
            Nodo sucesor = encontrarMinimo(nodo.derecho);
            nodo.dato = sucesor.dato;

            nodo.derecho = eliminar(
                    nodo.derecho,
                    sucesor.dato
            );
        }

        actualizarAltura(nodo);

        int balance = factorBalance(nodo);

        // Caso LL: rotacion simple a la derecha
        if (balance > 1
                && factorBalance(nodo.izquierdo) >= 0) {
            return rotacionDerecha(nodo);
        }

        // Caso LR: rotacion doble izquierda-derecha
        if (balance > 1
                && factorBalance(nodo.izquierdo) < 0) {
            nodo.izquierdo =
                    rotacionIzquierda(nodo.izquierdo);
            return rotacionDerecha(nodo);
        }

        // Caso RR: rotacion simple a la izquierda
        if (balance < -1
                && factorBalance(nodo.derecho) <= 0) {
            return rotacionIzquierda(nodo);
        }

        // Caso RL: rotacion doble derecha-izquierda
        if (balance < -1
                && factorBalance(nodo.derecho) > 0) {
            nodo.derecho =
                    rotacionDerecha(nodo.derecho);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    // Encontrar el menor valor de un subarbol
    private Nodo encontrarMinimo(Nodo nodo) {
        Nodo actual = nodo;

        while (actual.izquierdo != null) {
            actual = actual.izquierdo;
        }

        return actual;
    }

    // =========================
    // BUSQUEDA
    // =========================

    public boolean buscar(int dato) {
        Nodo actual = raiz;

        while (actual != null) {
            if (dato == actual.dato) {
                return true;
            }

            if (dato < actual.dato) {
                actual = actual.izquierdo;
            } else {
                actual = actual.derecho;
            }
        }

        return false;
    }

    // =========================
    // RECORRIDO PREORDEN
    // =========================

    public void preorden() {
        preorden(raiz);
        System.out.println();
    }

    private void preorden(Nodo nodo) {
        if (nodo != null) {
            System.out.print(nodo.dato + " ");
            preorden(nodo.izquierdo);
            preorden(nodo.derecho);
        }
    }

    // =========================
    // RECORRIDO INORDEN
    // =========================

    public void inorden() {
        inorden(raiz);
        System.out.println();
    }

    private void inorden(Nodo nodo) {
        if (nodo != null) {
            inorden(nodo.izquierdo);
            System.out.print(nodo.dato + " ");
            inorden(nodo.derecho);
        }
    }

    // =========================
    // RECORRIDO POSTORDEN
    // =========================

    public void postorden() {
        postorden(raiz);
        System.out.println();
    }

    private void postorden(Nodo nodo) {
        if (nodo != null) {
            postorden(nodo.izquierdo);
            postorden(nodo.derecho);
            System.out.print(nodo.dato + " ");
        }
    }

    // =========================
    // PROGRAMA PRINCIPAL
    // =========================

    public static void main(String[] args) {

        ArbolAVL arbol = new ArbolAVL();

        int[] valores = {
            30, 20, 10, 25, 28, 27, 50, 60
        };

        // Insertar valores
        for (int valor : valores) {
            arbol.insertar(valor);
        }

        System.out.println("===== ARBOL AVL INICIAL =====");

        System.out.print("Inorden: ");
        arbol.inorden();

        System.out.print("Preorden: ");
        arbol.preorden();

        System.out.print("Postorden: ");
        arbol.postorden();

        // Buscar valores
        System.out.println();
        System.out.println("===== BUSQUEDA =====");

        System.out.println(
                "Buscar 28: " + arbol.buscar(28)
        );

        System.out.println(
                "Buscar 100: " + arbol.buscar(100)
        );

        // Eliminar valores
        System.out.println();
        System.out.println("===== ELIMINACION =====");

        System.out.println("Eliminando 27...");
        arbol.eliminar(27);
        System.out.print("Inorden: ");
        arbol.inorden();

        System.out.println("Eliminando 25...");
        arbol.eliminar(25);
        System.out.print("Inorden: ");
        arbol.inorden();

        System.out.println("Eliminando 20...");
        arbol.eliminar(20);
        System.out.print("Inorden: ");
        arbol.inorden();

        // Mostrar estado final
        System.out.println();
        System.out.println("===== ARBOL AVL FINAL =====");

        System.out.print("Preorden: ");
        arbol.preorden();

        System.out.print("Inorden: ");
        arbol.inorden();

        System.out.print("Postorden: ");
        arbol.postorden();
    }
}
