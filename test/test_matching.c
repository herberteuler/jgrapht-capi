#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#include <jgrapht_capi.h>

#define ITERATOR_NO_SUCH_ELEMENT 100

int main() {
    graal_isolate_t *isolate = NULL;
    graal_isolatethread_t *thread = NULL;

    if (thread, graal_create_isolate(NULL, &isolate, &thread) != 0) {
        fprintf(stderr, "graal_create_isolate error\n");
        exit(EXIT_FAILURE);
    }

    void *g;
    jgrapht_capi_graph_create(thread, 0, 0, 0, 1, &g);

    long long v;
    long long e;
    jgrapht_capi_graph_add_vertex(thread, g, NULL);
    jgrapht_capi_graph_add_vertex(thread, g, NULL);
    jgrapht_capi_graph_add_vertex(thread, g, NULL);
    jgrapht_capi_graph_add_vertex(thread, g, NULL);

    jgrapht_capi_graph_add_edge(thread, g, 0, 1, NULL);
    jgrapht_capi_graph_add_edge(thread, g, 0, 3, NULL);
    jgrapht_capi_graph_add_edge(thread, g, 2, 1, NULL);
    jgrapht_capi_graph_add_edge(thread, g, 2, 3, NULL);

    jgrapht_capi_graph_set_edge_weight(thread, g, 0, 15.0);
    jgrapht_capi_graph_set_edge_weight(thread, g, 1, 5.0);
    jgrapht_capi_graph_set_edge_weight(thread, g, 2, 3.0);
    jgrapht_capi_graph_set_edge_weight(thread, g, 3, 100.0);

    void *m;
    double weight;
    long long card;
    jgrapht_capi_matching_exec_greedy_general_max_card(thread, g, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    assert(weight == 115.0);
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_matching_exec_custom_greedy_general_max_card(thread, g, 1, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_matching_exec_edmonds_general_max_card_dense(thread, g, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    assert(weight == 115.0);
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_matching_exec_edmonds_general_max_card_sparse(thread, g, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    // assert(weight == 115.0); bug in JGraphT 1.4.0 returns 2.0
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_matching_exec_greedy_general_max_weight(thread, g, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    assert(weight == 115.0);
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_matching_exec_custom_greedy_general_max_weight(thread, g, 1, 0.000000001, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    assert(weight == 115.0);
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_matching_exec_pathgrowing_max_weight(thread, g, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    assert(weight == 115.0);
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_matching_exec_blossom5_general_max_weight(thread, g, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    assert(weight == 115.0);
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_matching_exec_blossom5_general_min_weight(thread, g, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    assert(weight == 0.0);
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 0);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_matching_exec_blossom5_general_perfect_max_weight(thread, g, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    assert(weight == 115.0);
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_matching_exec_blossom5_general_perfect_min_weight(thread, g, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    assert(weight == 8.0);
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_matching_exec_bipartite_max_card(thread, g, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    // assert(weight == 115.0); bug in JGraphT 1.4.0 returns 2.0
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_matching_exec_bipartite_max_weight(thread, g, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    assert(weight == 115.0);
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    int isbipartite;
    void * part1, * part2;
    jgrapht_capi_partition_exec_bipartite(thread, g, &isbipartite, &part1, &part2);
    assert(isbipartite == 1);

    jgrapht_capi_matching_exec_bipartite_perfect_min_weight(thread, g, part1, part2, &m);
    jgrapht_capi_matching_get_weight(thread, m, &weight);
    assert(weight == 8.0);
    jgrapht_capi_matching_get_card(thread, m, &card);
    assert(card == 2);
    jgrapht_capi_destroy(thread, m);

    jgrapht_capi_destroy(thread, part1);
    jgrapht_capi_destroy(thread, part2);

    jgrapht_capi_destroy(thread, g);

    if (thread, graal_detach_thread(thread) != 0) {
        fprintf(stderr, "graal_detach_thread error\n");
        exit(EXIT_FAILURE);
    }

    return EXIT_SUCCESS;
}